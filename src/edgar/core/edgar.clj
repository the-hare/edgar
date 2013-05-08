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
            [edgar.core.analysis.filter :as afilter])
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

    (log/debug "edgar.core.edgar/feed-handler [" evt "] > tick-list size[" (count @tick-list) "] / [" (> (count @tick-list) 40) "] > options[" options "]")

    ;; handle tickPrice
    #_(if (= "tickPrice" (evt "type"))
      (handle-tick-price options evt)
      )


    ;; handle tickString
    (if (= "tickString" (evt "type"))
      (handle-tick-string options evt))


    ;; At the end of our 40 tick window
    ;;  - only for RTVolume last ticks
    ;;  - wrt a given tickerId
    (let [trimmed-list (->> @tick-list
                            (filter #(= "tickString" (% :type)) #_input_here )
                            (filter #(= (evt "tickerId") (% :tickerId)) #_input_here))
          tail-evt (first trimmed-list)]


      (log/debug "edgar.core.edgar/feed-handler VS > trimmed[" (count trimmed-list) "][" #_trimmed-list "] tick-list[" (count @tick-list) "][" #_@tick-list "]")

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
      (afilter/simple-moving-average 20 @tick-list))

    (log/debug "**** PRINTING our SMA [" sma "]")
    ))


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect)
        hdata (load-historical-data edatomic/conn)

        tick-list (ref [])]

    (market/subscribe-to-market (partial feed-handler {:tick-list tick-list}))
    (market/request-market-data client 0 (-> hdata last second) "233" false)
    ;;(market/request-market-data client 0 "IBM" "233" false)
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
