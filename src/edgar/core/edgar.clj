(ns edgar.core.edgar
  (:use [clojure.repl]
        [clojure.core.strint]
        [clojure.tools.namespace.repl]
        [datomic.api :only [q db] :as d])
  (:require [clojure.tools.logging :as log]
            [clojure.walk :as walk]
            [clojure.string :as cstring]
            [clojure.pprint :as pprint]
            [cljs-uuid.core :as uuid]
            [edgar.datomic :as edatomic]
            [edgar.ib.market :as market]
            [edgar.tee.datomic :as tdatomic]
            [edgar.core.analysis.lagging :as lagging])
  )


(defn load-historical-data [conn]

  ;; find entity.symbol (and entire entity) where price-difference is greatest
  (let [historical-entities (q '[:find ?p ?s ?c :where
                                 [?h :historical/price-difference ?p]
                                 [?h :historical/symbol ?s]
                                 [?h :historical/company ?c]
                                 ] (db conn))]
    (sort-by first historical-entities)
    )
  )


(defn handle-tick-price
  " Format will look like:

    {type tickPrice, tickerId 0, timeStamp #<DateTime 2013-05-01T13:29:38.129-04:00>, price 412.14, canAutoExecute 0, field 4}
  "
  [options evt]

  (dosync (alter (:tick-list options)
                 (fn [inp] (conj inp (walk/keywordize-keys (merge evt {:uuid (str (uuid/make-random))}))))))
  )
(defn handle-tick-string
  "Format will look like:

   {type tickString, tickerId 0, tickType 48, value 412.14;1;1367429375742;1196;410.39618025;true}
  "
  [options evt]

  (log/debug "handle-tick-string > options[" options "] evt[" evt "]")
  (let [tkeys [:last-trade-price :last-trade-size :last-trade-time :total-volume :vwap :single-trade-flag]
        tvalues (cstring/split (evt "value") #";")  ;; parsing RTVolume data
        result-map (zipmap tkeys tvalues)
        ]

    (dosync (alter (:tick-list options)
                   (fn [inp] (conj inp (merge result-map {:tickerId (evt "tickerId")
                                                         :type (evt "type")
                                                         :uuid (str (uuid/make-random))}) ))))
     ))

(defn feed-handler
  "Event structures will look like 'tickPrice' or 'tickString'"
  [options evt]


  (let [tick-list (:tick-list options)]

    (log/debug "edgar.core.edgar/feed-handler [" evt "] > tick-list size[" (count @tick-list) "] > [" @tick-list "] > options[" #_options "]")

    ;; handle tickPrice
    #_(if (= "tickPrice" (evt "type"))
      (handle-tick-price options evt)
      )


    ;; handle tickString
    (if (and (= "tickString" (evt "type"))
             (= 48 (evt "tickType")))
      (handle-tick-string options evt))


    ;; At the end of our 40 tick window
    ;;  - only for RTVolume last ticks
    ;;  - wrt a given tickerId
    (let [trimmed-list (->> @tick-list
                            (filter #(= "tickString" (% :type)) #_input_here )
                            (filter #(= (evt "tickerId") (% :tickerId)) #_input_here))
          tail-evt (first trimmed-list)]


      (log/debug "")
      (log/debug "")
      #_(log/debug "edgar.core.edgar/feed-handler VS > trimmed[" (count trimmed-list) "][" trimmed-list "] tick-list[" (count @tick-list) "][" @tick-list "]")

      ;; i. spit the data out to DB and
      ;; ii. and trim the list list back to 40
      (if (> (count trimmed-list) 40)

          (do
            (tdatomic/tee-market tail-evt)
            (dosync (alter tick-list
                           (fn [inp] (into []
                                          (remove #(= (:uuid tail-evt) (% :uuid)) inp))))))))




    ;; the recieving data structure, should allow me to apply a strategy to:
    ;; 1. plot bid / ask data on an x / y graph
    ;; 2. overlay SMA on the same graph
    ;; 3. overlay an EMA on the same graph

    ;; strategy should identify when trend lines cross-over

    ;; i. 20 tick structure & ii. strategy should allow me to extrude this to a clojurescript front-end

    (def sma
      (lagging/simple-moving-average 5 @tick-list))
    #_(log/debug "**** PRINTING our SMA [" sma "]")

    (def ema
      (lagging/exponential-moving-average 5 @tick-list))
    #_(log/debug "*** PRINTING our EMA [" ema "]")

    (def bollinger
      (lagging/bollinger-band 5 @tick-list))
    #_(log/debug "*** PRINTING our Bollinger Band [" bollinger "]")
    ))


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect)
        hdata (load-historical-data edatomic/conn)

        tick-list (ref [])]

    (market/subscribe-to-market (partial feed-handler {:tick-list tick-list}))
    ;;(market/request-market-data client 0 (-> hdata last second) "233" false)

    (market/request-market-data client 0 "IBM" "233" false)
    ))

(defn fubar []
  (test-run))

#_(def historical-ids (q '[:find ?p ?s ?c :where
                         [?h :historical/price-difference ?p]
                         [?h :historical/symbol ?s]
                         [?h :historical/company ?c]
                         ] (db edatomic/conn)))
;;(def the-list (into [] historical-ids))
;;(def thing (sort-by first the-list))
;;(def allthings (doall (map println thing)))

;;(def historical-entities (map (fn [[hid]] (d/entity (db edatomic/conn) hid)) historical-ids))
;;(def historical-sorted (sort-by :historical/price-difference historical-entities))

;; (def tl [{:uuid "44c63d93-b600-4b2f-afdf-5c0961e896ee" :type "tickString" :tickerId 0, :single-trade-flag true, :vwap 418.34010978, :total-volume 757, :last-trade-time 1368033338841, :last-trade-size 1, :last-trade-price 417.76} {:uuid "46711827-baa1-46d3-95e9-9d5821832ac8" :type "tickString" :tickerId 0, :single-trade-flag false, :vwap 418.3249716, :total-volume 771, :last-trade-time 1368033346313, :last-trade-size 14, :last-trade-price 417.51} {:uuid "5a1d25e5-0565-4a74-bbbb-89add95e51ca" :type "tickString" :tickerId 0, :single-trade-flag false, :vwap 418.32287594, :total-volume 773, :last-trade-time 1368033346544, :last-trade-size 2, :last-trade-price 417.51} {:uuid "c6329f53-05f3-46ae-9492-af772ee4d05f" :type "tickString" :tickerId 0, :single-trade-flag false, :vwap 418.32041047, :total-volume 775, :last-trade-time 1368033348872, :last-trade-size 2, :last-trade-price 417.37} {:uuid "bc7865ff-e2c2-4069-a1bb-768bf4d95e62" :type "tickString" :tickerId 0, :single-trade-flag false, :vwap 418.30192265, :total-volume 787, :last-trade-time 1368033370650, :last-trade-size 12, :last-trade-price 417.11} {:uuid "49b43087-9513-400e-886b-929c75dedfab" :type "tickString" :tickerId 0, :single-trade-flag true, :vwap 418.30038468, :total-volume 788, :last-trade-time 1368033383945, :last-trade-size 1, :last-trade-price 417.09} {:uuid "05b1aef2-c8f6-48ec-9c84-7fc1f6b3fc34" :type "tickString" :tickerId 0, :single-trade-flag false, :vwap 418.29733307, :total-volume 790, :last-trade-time 1368033384976, :last-trade-size 2, :last-trade-price 417.10}])
