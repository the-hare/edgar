(ns edgar.datomic-test
  (:use [midje.sweet])
  (:require [edgar.datomic :as edatomic]))


(with-state-changes [(after :facts (edatomic/database-delete "datomic:mem://hello"))]

  (fact "Test that we can create a (in-memory) database"
        (let [result (edatomic/database-create "datomic:mem://edgar")]
          result => true))

  (fact "Test that we can connect to that (in-memory database)"
        (let [result (edatomic/database-connect "datomic:mem://edgar")]

          result =not=> nil?
          (type result) => datomic.peer.LocalConnection)))
