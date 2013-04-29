(ns edgar.core.edgar
  (:use [clojure.repl]
        [clojure.core.strint]
        [datomic.api :only [q db] :as d])
  (:require [edgar.datomic :as edatomic]
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

  )

(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        hdata (load-historical-data client)]

    (market/request-market-data client 0 (-> hdata last second) false)
    ))
