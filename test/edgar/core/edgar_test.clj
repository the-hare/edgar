(ns edgar.core.edgar-test
  (:use [midje.sweet])
  (:require [edgar.core.edgar :as edgar]
            [edgar.datomic :as edatomic]
            [edgar.tee.datomic :as tee]))



;; ... TODO


;; setup a local Datomic connection
#_(def conn (atom nil))


#_(with-state-changes [(before :facts (do (edatomic/database-create "datomic:mem://edgar")
                                        (reset! conn (edatomic/database-connect "datomic:mem://edgar"))
                                        (edatomic/database-schema-create @conn)
                                        (tee/tee-historical @conn (read-string (slurp "etc/test-historical-list.edn")))
                                        ))]

  (println "... Grrr[" @conn "]")
  (fact "Test that we can list high moving stocks"
              (count (edgar/load-historical-data 10 @conn)) => 10
              (count (edgar/load-historical-data 20 @conn)) => 20)

  )


#_(with-state-changes [(before :facts (edatomic/database-create "datomic:mem://edgar"))]

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
