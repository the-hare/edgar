(ns edgar.core.edgar-test
  (:use [midje.sweet])
  (:require [edgar.core.edgar :as edgar]
            [edgar.datomic :as edatomic]
            [edgar.tee.datomic :as tee]))


;; setup a local Datomic connection
(def conn (atom nil))


(with-state-changes [(before :facts (edatomic/database-create "datomic:mem://edgar"))]

  (fact "stub.1" 1 => 1)
  (with-state-changes [(before :facts (reset! conn (edatomic/database-connect "datomic:mem://edgar")))]

    (fact "stub.2" 2 => 2)
    (with-state-changes [(before :facts (edatomic/database-schema-create @conn))]

      (fact "stub.3" 3 => 3)
      (with-state-changes [(before :facts (tee/tee-historical @conn (read-string (slurp "etc/test-historical-list.edn"))))]

        (fact "Test that we can list high moving stocks"
              (count (edgar/load-historical-data 10 conn)) => 10
              (count (edgar/load-historical-data 20 conn)) => 20)
        )))
  )

;; ... TODO - get a test-historical list, as formatted by IB


;; ... TODO - create Pedestal service tests (see https://github.com/pedestal/pedestal/blob/master/service/test/io/pedestal/service/http_test.clj)
;;  -- test HTTP method(s)
;;  -- test request inputs
;;  -- test pedestal response value
;;  -- test pedestal response type (edn)

;; ... TODO - create web client tests (see pedestal/app/test/io/pedestal/test/app.clj)
