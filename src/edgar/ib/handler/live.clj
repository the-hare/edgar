(ns edgar.ib.handler.live
  (:use [clojure.repl]
        [clojure.core.strint]
        [clojure.tools.namespace.repl]
        [datomic.api :only [q db] :as d])
  (:require [clojure.tools.logging :as log]
            [clojure.walk :as walk]
            [clojure.string :as cstring]
            [clojure.pprint :as pprint]
            [cljs-uuid.core :as uuid]
            [edgar.tee.datomic :as tdatomic]
            [edgar.core.analysis.lagging :as lagging])
  )

(defn load-filtered-results
  "Find entity.symbol (and entire entity) where price-difference is greatest"
  [limit conn]

  (let [historical-entities (q '[:find ?p ?s ?c :where
                                 [?h :historical/price-difference ?p]
                                 [?h :historical/symbol ?s]
                                 [?h :historical/company ?c]
                                 ] (db conn))
        sorted-entities (reverse (sort-by first historical-entities))]

    (if limit
      (take limit sorted-entities)
      sorted-entities)
    )
  )


(defn handle-tick-price
  " Format will look like:

    {type tickPrice, tickerId 0, timeStamp #<DateTime 2013-05-01T13:29:38.129-04:00>, price 412.14, canAutoExecute 0, field 4}
  "
  [options evt]

  (dosync (alter (:tick-list options)
                 (fn [inp] (conj inp (walk/keywordize-keys (merge evt {:uuid (str (uuid/make-random))})))))))

(defn handle-tick-string
  "Format will look like:

   {type tickString, tickerId 0, tickType 48, value 412.14;1;1367429375742;1196;410.39618025;true}
  "
  [options evt]

  (log/debug "handle-tick-string > options[" options "] evt[" evt "]")
  (let [tkeys [:last-trade-price :last-trade-size :last-trade-time :total-volume :vwap :single-trade-flag]
        tvalues (cstring/split (evt "value") #";")  ;; parsing RTVolume data
        result-map (zipmap tkeys tvalues)]

    (dosync (alter (:tick-list options)
                   (fn [inp] (conj inp (merge result-map {:tickerId (evt "tickerId")
                                                         :type (evt "type")
                                                         :uuid (str (uuid/make-random))}) ))))))

(defn handle-event [options evt]

  (let [tick-list (:tick-list options)
        tee-list (if (:tee-list options)
                   (conj (:tee-list options) tdatomic/tee-market)
                   [tdatomic/tee-market])
        tick-window (if (:tick-window options)
                      (:tick-window options)
                      40)]

    (log/debug "edgar.core.edgar/handle-event [" evt "] FILTER[" (:ticker-id-filter options) "] > tick-list size[" (count @tick-list) "] > [" @tick-list "] > options[" #_options "]")


    ;; handle tickPrice
    #_(if (= "tickPrice" (evt "type")) (handle-tick-price options evt))


    ;; handle tickString
    (if (and (= "tickString" (evt "type"))
             (= 48 (evt "tickType")))
      (handle-tick-string options evt))


    ;; At the end of our tick window
    ;;  - only for RTVolume last ticks
    ;;  - wrt a given tickerId
    (let [trimmed-list (->> @tick-list
                            (filter #(= "tickString" (% :type)) #_input_here )
                            (filter #(= (evt "tickerId") (% :tickerId)) #_input_here))
          tail-evt (first trimmed-list)]


      (log/debug "")
      (log/debug "")
      (log/debug "edgar.core.edgar/handle-event VS > trimmed[" (count trimmed-list) "][" "] tick-list[" (count @tick-list) "][" "] > CHECK[" (>= (count trimmed-list) tick-window) "]")


      ;; i. spit the data out to DB and
      ;; ii. and trim the list list back to the tick-window size
      (if (>= (count trimmed-list) tick-window)

        (do


          (reduce (fn [rslt efn]
                    (efn trimmed-list (first trimmed-list)))
                  nil
                  tee-list)


          ;; trim down tick-list if it's greater than 200
          (if (>= (count trimmed-list) 200)
            (dosync (alter tick-list
                           (fn [inp] (into []
                                          (remove #(= (:uuid tail-evt) (% :uuid)) inp)))))))))




    ;; the recieving data structure, should allow me to apply a strategy to:
    ;; 1. plot bid / ask data on an x / y graph
    ;; 2. overlay SMA on the same graph
    ;; 3. overlay an EMA on the same graph

    ;; strategy should identify when trend lines cross-over

    ;; i. 20 tick structure & ii. strategy should allow me to extrude this to a clojurescript front-end

    (def sma
      (lagging/simple-moving-average nil 5 @tick-list)) #_(log/debug "**** PRINTING our SMA [" sma "]")

    (def ema
      (lagging/exponential-moving-average nil 5 @tick-list)) #_(log/debug "*** PRINTING our EMA [" ema "]")

    (def bollinger
      (lagging/bollinger-band 5 @tick-list)) #_(log/debug "*** PRINTING our Bollinger Band [" bollinger "]")))


(defn feed-handler
  "Event structures will look like 'tickPrice' or 'tickString'

   Options are:
     :tick-list - the list into which result tick events will be put
     :tee-list - list of pipes to which result events will be pushed
     :ticker-id-filter - a list of tickerIds about which this feed-handler cares"
  [options evt]

  (log/info "feed-handler > tickerIDs[" (:ticker-id-filter options) "] > tick-list SIZE[" (count (deref (:tick-list options))) "] > SOME[" (some #{ (evt "tickerId") } (:ticker-id-filter options)) "]")
  (if (:ticker-id-filter options)

    ;; check if this event passes the filter
    (if (some #{ (evt "tickerId") }
              (:ticker-id-filter options))

      (handle-event (assoc options :tick-window 40) evt)
      nil)
    (handle-event (assoc options :tick-window 40) evt))
  )
