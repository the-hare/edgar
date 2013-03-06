(ns edgar.datomic
  
  (:use [datomic.api :only [q db] :as d])
)


(defn database-create []
  (def uri "datomic:free://localhost:4334/edgar")
  (d/create-database uri)
)


(defn database-connect []
  (def conn (d/connect uri))
)


(defn database-schema-create []
  (def schema-tx (read-string (slurp "config/edgar-schema.dtm")))
  @(d/transact conn schema-tx)
)


(defn insert-data []
  
  ;; parse seed data dtm file
  (def data-tx (read-string (slurp "samples/seattle/seattle-data0.dtm")))
  
  ;; display first three statements in seed data transaction
  (first data-tx)
  (second data-tx)
  (nth data-tx 2)
  
  ;; submit seed data transaction
  @(d/transact conn data-tx)
)

