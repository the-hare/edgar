(ns edgar.datomic

  (:use [datomic.api :only [q db] :as d])
)

(def url "datomic:free://localhost:4334/edgar")

(defn database-create [iurl]
  (d/create-database (if iurl iurl url)))
(defn database-delete [iurl]
  (d/delete-database (if iurl iurl url)))


(defn database-connect [iurl]
  (d/connect (if iurl iurl url)))


(defn database-schema-create [conn]
  (def schema-tx (read-string (slurp "etc/edgar-schema.dtm")))
  @(d/transact conn schema-tx)
)


(defn insert-data [conn]

  {:pre (not (nil? conn))}

  ;; parse seed data dtm file
  (def data-tx (read-string (slurp "samples/seattle/seattle-data0.dtm")))

  ;; display first three statements in seed data transaction
  (first data-tx)
  (second data-tx)
  (nth data-tx 2)

  ;; submit seed data transaction
  @(d/transact conn data-tx)
)
