(ns edgar.ib.market
  (:import [com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution])
  (:use [clojure.core.strint])
  (:require [edgar.eclientsocket :as socket]
            [lamina.core :as lamina]
            [overtone.at-at :as at]
            [clj-time.core :as cime]
            [clj-time.local :as time]
            [clj-time.format :as format])
  )

(defn connect-to-market
  "Connect to the IB marketplace. This should return a 'client' object"
  []
  (socket/connect-to-tws))


(defn- create-contract [instrm]

  (let [contract (Contract.)]
    (set! (.m_symbol contract) instrm)
    (set! (.m_secType contract) "STK")
    (set! (.m_exchange contract) "SMART")
    (set! (.m_currency contract) "USD")

    contract)
  )

;; ====
;; HISTORICAL Data
(defn request-historical-data
  "Request historical historical information in the form of a feed or data snapshot.

   See function reference here:
     http://www.interactivebrokers.com/php/apiUsersGuide/apiguide/java/reqhistoricaldata.htm"

  ([client idx instrm]
     (request-historical-data client idx instrm "1 D" "1 day" "TRADES"))

  ([client idx instrm , duration-str bar-size what-to-show]
     (let [contract (create-contract instrm)
           nnow (time/local-now)
           tformat (format/formatter "yyyyMMdd HH:mm:ss z")
           tstring (format/unparse tformat nnow)
           ]
       (.reqHistoricalData client idx contract tstring duration-str bar-size what-to-show 0 1)
       )
     ))
(defn cancel-historical-data
  "Cancel the request ID, used in 'request-historical-data'"
  [client idx]

  (.cancelHistoricalData client idx))



;; ====
;; MARKET Data
(defn request-market-data
  "Request historical market information in the form of a feed or data snapshot"

  ([client idx instrm]
     (request-market-data client idx instrm "" false))

  ([client idx instrm genericTicklist snapshot]
     (let [contract (create-contract instrm)]

       (.reqMktData client idx contract genericTicklist snapshot))
     ))
(defn cancel-market-data
  "Cancel the request ID, used in 'request-market-data'"
  [client idx]

  (.cancelMktData client idx))


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
