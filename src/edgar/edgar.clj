(ns edgar.edgar
  (:import (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution))
  (:use [clojure.repl])
  (:require [edgar.eclientsocket :as socket]
            [edgar.ewrapper :as ewrapper])
  )


(defn connect []

  (def client (socket/connect-to-tws))
)
(defn getMarketData []
  #_(let [
        ;; create a Contract
        ;; int p_conId, String p_symbol, String p_secType, String p_expiry,
        ;; double p_strike, String p_right, String p_multiplier,
        ;; String p_exchange, String p_currency, String p_localSymbol,
        ;; Vector p_comboLegs, String p_primaryExch, boolean p_includeExpired,
        ;; String p_secIdType, String p_secId
        contract (Contract. 0, "IBM", "STK", nil,
                            0.0, nil, nil,
                            "SMART", "USD", nil,
                            nil, nil, false,
                            nil, nil)

        ;; request Market Data
        xxx (.reqMktData client 0 contract nil false)
       ]
      )

  (connect)
  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData client 0 contract nil false))
)
