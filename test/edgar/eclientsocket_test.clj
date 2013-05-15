(ns edgar.eclientsocket-test
  (:use [midje.sweet])
  (:require [edgar.eclientsocket :as socket]))


(with-state-changes [(after :facts (socket/disconnect-from-tws))]
  (fact "Testing result map from calling the Interactive Brokers API Gateway"

        (let [result (socket/connect-to-tws)]

          result => map?
          (:esocket result) =not=> nil?
          (:ewrapper result) =not=> nil?
          ) ))

(with-state-changes [(after :facts (socket/disconnect-from-tws))]
  (fact "Testing that there's an actual connection to the Interactive Brokers API Gateway"

        ))
