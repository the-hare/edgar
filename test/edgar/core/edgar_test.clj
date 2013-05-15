(ns edgar.core.edgar-test
  (:use [midje.sweet])
  (:require [edgar.core.edgar :as edgar]
            [edgar.datomic :as edatomic]))


;; setup a local Datomic connection
(def conn (atom nil))


;; ... TODO - create Datomic in memory connection
;; ... TODO - create database
;; ... TODO - create schema

;; ... TODO - before each test, populate with about 100 records
(with-state-changes [(before :facts (do ((edatomic/database-create "datomic:mem://edgar"))
                                        (reset! conn )))
                     ]

  #_(fact "Test that we can list high moving stocks"

        (count (edgar/load-historical-data conn 10)) => 10
        (count (edgar/load-historical-data conn 20)) => 20))



;; ... TODO - create Pedestal service tests (see pedestal/app/test/io/pedestal/test/app.clj)
;;  -- test HTTP method(s)
;;  -- test request inputs
;;  -- test pedestal response value
;;  -- test pedestal response type (edn)

;; ... TODO - create web client tests (see pedestal/app/test/io/pedestal/test/app.clj)
