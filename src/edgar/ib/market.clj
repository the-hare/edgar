(ns edgar.ib.market
  (:import [com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution])
  (:require [edgar.eclientsocket :as socket]
            [lamina.core :as lamina]
            [overtone.at-at :as at])
  )

(defn connect-to-market
  "Connect to the IB marketplace. This should return a 'client' object"
  []
  (socket/connect-to-tws))

(defn request-market-data
  "Request market information in the form of a feed or data snapshot"
  ([client idx instrm]
     (request-market-data client idx instrm false))

  ([client idx instrm snapshot]
     (let [contract (Contract.)]
       (set! (.m_symbol contract) instrm)
       (set! (.m_secType contract) "STK")
       (set! (.m_exchange contract) "SMART")
       (set! (.m_currency contract) "USD")

       (.reqMktData client idx contract "" snapshot)))
  )

(defonce event-channel (ref (lamina/channel)))
(defn subscribe-to-market [handle-fn]
  (lamina/receive-all @event-channel handle-fn)
  )

(defn publish-event
  [^clojure.lang.PersistentHashMap event]

  (lamina/enqueue @event-channel event)
)
(defn publish-event-from-java
  [^java.util.HashMap event]

  (publish-event (merge {} event))  ;; transform java.util.HashMap to a Clojure map
)

;; ==========
(defn test-publisher []

  (subscribe-to-market #(println "handling: " %))

  (def my-pool (at/mk-pool))
  (at/every 1000 (fn [] (publish-event { :tickerId 0 :field 1 :price 5.75 :canAutoExecute 1})) my-pool) )
;; ==========
