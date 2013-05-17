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
            [edgar.ib.handler.live :as live]
            [edgar.core.analysis.lagging :as lagging])
  )


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect)
        hdata (live/load-historical-data conn)

        tick-list (ref [])]

    (market/subscribe-to-market (partial live/feed-handler {:tick-list tick-list}))
    ;;(market/request-market-data client 0 (-> hdata last second) "233" false)

    (market/request-market-data client 0 "IBM" "233" false)
    ))

(defn fubar []
  (test-run))
