(ns edgar.core.edgar
  (:use [clojure.repl]
        [clojure.core.strint]
        [datomic.api :only [q db] :as d])
  (:require [clojure.tools.logging :as log]
            [edgar.datomic :as edatomic]
            [edgar.ib.market :as market])
  )


(defn load-historical-data []

  ;; find entity.symbol (and entire entity)
  ;; where price-difference is greatest
  (let [historical-entities (q '[:find ?p ?s ?c :where
                                 [?h :historical/price-difference ?p]
                                 [?h :historical/symbol ?s]
                                 [?h :historical/company ?c]
                                 ] (db edatomic/conn))]
    (sort-by first historical-entities)
    )

  )

(defn feed-handler [evt]


  (log/debug "edgar.core.edgar/feed-handler [" evt "]")

  ;; ... TODO

  ;; determine a data structure that can contain the last 20 running ticks

  ;; at the end of our 20 tick window, spit the data out to DB

  ;; the recieving data structure, should allow me to apply a strategy to:
  ;; 1. plot bid / ask data on an x / y graph
  ;; 2. overlay SMA on the same graph
  ;; 3. overlay an EMA on the same graph

  ;; strategy should identify when trend lines cross-over

  ;; i. 20 tick structure & ii. strategy should allow me to extrude this to a clojurescript front-end

  )


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        hdata (load-historical-data client)]

    (market/subscribe-to-market feed-handler)
    (market/request-market-data client 0 (-> hdata last second) false)
    ))






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
