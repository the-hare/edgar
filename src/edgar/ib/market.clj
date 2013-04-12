(ns edgar.ib.market
  (:import [com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution])
  (:require [edgar.eclientsocket :as socket]
            )

  )

(defn connect-to-market
  "Connect to the IB marketplace. This should return a 'client' object"
  []
  (socket/connect-to-tws))

(defn request-market-data [client idx instrm]
  (.reqMktData client idx (Contract. idx, instrm, "STK", nil, 0.0, nil, nil, "SMART", "USD", nil, nil, nil, false, nil, nil) nil false)
  )
