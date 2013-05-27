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
            [edgar.ib.handler.historical :as historical]
            [edgar.tee.datomic :as tdatomic]
            [edgar.tee.play :as tplay]
            [edgar.ib.handler.live :as live]
            [edgar.core.analysis.lagging :as lagging]))


(defn play-historical
  "1) takes a selection of stock symbols
   2) gets historical market data
   3) plays back over the results

   You can optionally pass in these time arguments.

     time-duration: the entire length of time for which historical data is returned
     time-intervals: the time intervals into which tick data is returned

   A) See IB docs: http://www.interactivebrokers.com/php/apiUsersGuide/apiguide/java/reqhistoricaldata.htm
   B) There are also Historical Data Limitations: http://www.interactivebrokers.com/php/apiUsersGuide/apiguide/api/historical_data_limitations.htm"

  ([client stock-selection]
     (play-historical client stock-selection "1 D" "1 day" nil))
  ([client stock-selection time-duration time-interval tee-fn-list]

     {:pre [(not (nil? client))
            (not (nil? stock-selection))]}

     (let [bucket (ref [])
           options {:bucket bucket
                    :client client
                    :tee-list (if tee-fn-list tee-fn-list [tplay/tee-historical])
                    :stock-lists stock-selection
                    :tranche-size 60
                    :scheduler-options {:min 10.5}
                    :time-duration time-duration
                    :time-interval time-interval}]

       (market/subscribe-to-market (partial historical/snapshot-handler options))
       (historical/schedule-historical-data options))))

(defn play-live
  "1) takes a selection of stock symbols
   2) gets a live market feed
   3) plays back the results in real-time"
  ([client stock-selection]
     (play-live client stock-selection nil))
  ([client stock-selection tee-fn-list]

     {:pre [(not (nil? client))
            (not (nil? stock-selection))]}

     (reduce (fn [req-id ech]

               (let [tick-list (ref [])
                     tee-list (if tee-fn-list tee-fn-list [(partial tplay/tee-market @tick-list)])
                     options {:tick-list tick-list :tee-list tee-list :ticker-id-filter [req-id]}]

                 (market/subscribe-to-market (partial live/feed-handler options))
                 (market/request-market-data client req-id ech "233" false)

                 (inc req-id)  ;; increment the request ID for the next stock symbol
                 ))
             0
             stock-selection)))

(defn initialize-workbench []
  (def *interactive-brokers-workbench* {:interactive-brokers-client (:esocket (market/connect-to-market))})
  *interactive-brokers-workbench*)

(defn refresh-workbench []

  (market/close-market-channel)
  (market/disconnect-from-market)
  (refresh)
  (market/create-event-channel)
  (initialize-workbench))

(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect nil)
        hdata (live/load-filtered-results nil conn)

        tick-list (ref [])
        tid-filter [0]]

    (market/subscribe-to-market (partial live/feed-handler {:tick-list tick-list :ticker-id-filter tid-filter}))
    ;;(market/request-market-data client 0 (-> hdata last second) "233" false)
    (market/request-market-data client 0 "IBM" "233" false)))

(defn test-play-live []
  (let [workbench (initialize-workbench)
        client (:interactive-brokers-client workbench)]
    (play-live client ["DDD" #_"IBM" #_"AAPL"])))

(defn test-play-historical []
  (let [workbench (initialize-workbench)
        client (:interactive-brokers-client workbench)
        stock-list [["DDD" "3D Systems Corporation" "35.12" "2155763549.68" "n/a" "n/a" "Technology" "Computer Software: Prepackaged Software" "http://www.nasdaq.com/symbol/ddd" ""]
                    ["MMM" "3M Company" "102.31" "70592902989.05" "n/a" "n/a" "Health Care" "Medical/Dental Instruments" "http://www.nasdaq.com/symbol/mmm" ""]]]
    (play-historical client stock-list "1800 S" "1 secs")))

(defn fubar []
  (test-run)
  (test-play-live)
  (test-play-historical))
