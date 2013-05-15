(ns edgar.core.edgar-test
  (:use [midje.sweet])
  (:require [edgar.core.edgar :as edgar]
            [edgar.datomic :as edatomic]
            [edgar.tee.datomic :as tee]))


;; setup a local Datomic connection
(def conn (atom nil))


(defn populate-test-data [cxn forms]
  (tee/tee-historical cxn forms))


(with-state-changes [(before :facts (edatomic/database-create "datomic:mem://edgar"))]
  (with-state-changes [(before :facts (edatomic/database-schema-create conn))]
    (with-state-changes [(before :facts (populate-test-data conn (read-string (slurp "etc/test-historical-list.edn"))))])


    #_ (fact "Test that we can list high moving stocks"

            (count (edgar/load-historical-data conn 10)) => 10
            (count (edgar/load-historical-data conn 20)) => 20)))



;; ... TODO - create Pedestal service tests (see pedestal/app/test/io/pedestal/test/app.clj)
;;  -- test HTTP method(s)
;;  -- test request inputs
;;  -- test pedestal response value
;;  -- test pedestal response type (edn)

;; ... TODO - create web client tests (see pedestal/app/test/io/pedestal/test/app.clj)
