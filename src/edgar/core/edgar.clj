(ns edgar.core.edgar
  (:use [clojure.repl]
        [clojure.core.strint]
        [datomic.api :only [q db] :as d])
  (:require [edgar.datomic :as edatomic])
  )


(defn asdf []

  ;; find entity.symbol (and entire entity)
  ;; where price-difference is greatest

  (def historical-ids (q '[:find ?p ?s ?c :where
                           [?h :historical/price-difference ?p]
                           [?h :historical/symbol ?s]
                           [?h :historical/company ?c]
                           ] (db edatomic/conn)))

  (def the-list (into [] historical-ids))
  (def thing (sort-by first the-list))
  (def allthings (doall (map println thing)))


  ;;(def historical-entities (map (fn [[hid]] (d/entity (db edatomic/conn) hid)) historical-ids))
  ;;(def historical-sorted (sort-by :historical/price-difference historical-entities))


  )
