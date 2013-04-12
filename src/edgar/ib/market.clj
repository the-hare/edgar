(ns edgar.ib.market
  (:import [com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution])
  (:require [edgar.eclientsocket :as socket]
            )

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
