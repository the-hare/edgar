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
            [edgar.tee.play :as tplay]
            [edgar.ib.handler.live :as live]
            [edgar.core.analysis.lagging :as lagging])
  )




(defn play-historical
  ""
  [stock-selection]

  )

(defn play-live
  "1) takes a selection of stock symbols
   2) gets a live market feed
   3) plays back the results in real-time"
  [stock-selection]

  (map (fn [ech]

         (let [tick-list (ref [])
               tee-list [(partial tplay/tee-market tick-list)]]

           (market/subscribe-to-market (partial live/feed-handler {:tick-list tick-list :tee-list tee-list}))
           ;;(market/request-market-data *client* *req-id* *stock-sym* "233" false)
           )
         )

       stock-selection)
  )

(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect nil)
        hdata (live/load-filtered-results nil conn)

        tick-list (ref [])]

    (market/subscribe-to-market (partial live/feed-handler {:tick-list tick-list}))
    ;;(market/request-market-data client 0 (-> hdata last second) "233" false)

    (market/request-market-data client 0 "IBM" "233" false)
    ))

(defn fubar []
  (test-run))
