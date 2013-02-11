(ns edgar.ewrapper
  (:import (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution))
  (:use [clojure.core.strint])
)

(defn create-ewrapper
  "When reifying Java interface methods, we need the 'this' arg and I find you need to hint the exact types when reifying java interfaces"
  []

  (reify EWrapper


    ;; Connection & Server
    (^void currentTime [_, ^long time] (println "..."))
    (^void error [_, ^int id, ^int errorCode, ^String errorString]

      (println (<< "EWrapper.error CALLED > this[~{_}] > id[~{id}] > errorCode[~{errorCode}] > errorString[~{errorString}]")))

    (^void error [_, ^String error] (println "..."))
    (^void connectionClosed [_] (println "..."))


    ;; Market Data
    (^void tickPrice [_, ^int tickerId, ^int field, ^double price, ^int canAutoExecute] (println "..."))


    (^void tickSize [_, ^int tickerId, ^int field, ^int size] (println "..."))
    (^void tickOptionComputation [_, ^int tickerId, ^int field, ^double impliedVol, ^double delta, ^double optPrice, ^double pvDividend, ^double gamma, ^double vega, ^double theta, ^double undPrice] (println "..."))
    (^void tickGeneric [_, ^int tickerId, ^int tickType, ^double value] (println "..."))
    (^void tickString[_, ^int tickerId, ^int tickType, ^String value] (println "..."))
    (^void tickEFP [_, ^int tickerId, ^int tickType, ^double basisPoints, ^String formattedBasisPoints, ^double impliedFuture, ^int holdDays, ^String futureExpiry, ^double dividendImpact, ^double dividendsToExpiry] (println "..."))


    ;; Orders
    (^void orderStatus [_, ^int orderId, ^String status, ^int filled, ^int remaining, ^double avgFillPrice, ^int permId, ^int parentId, ^double lastFillPrice, ^int clientId, ^String whyHeld] (println "..."))
    (^void openOrder [_, ^int orderId, ^Contract contract, ^Order order, ^OrderState orderState] (println "..."))
    (^void nextValidId [_, ^int orderId] (println "..."))


    ;; Account & Portfolio
    (^void updateAccountValue [_, ^String key, ^String value, ^String currency, ^String accountName] (println "..."))
    (^void updatePortfolio [_, ^Contract contract, ^int position, ^double marketPrice, ^double marketValue, ^double averageCost, ^double unrealizedPNL, ^double realizedPNL, ^String accountName] (println "..."))
    (^void updateAccountTime [_, ^String timeStamp] (println "..."))


    ;; Contract Details
    (^void contractDetails [_, ^int reqId, ^ContractDetails contractDetails] (println "..."))
    (^void contractDetailsEnd [_, ^int reqId] (println "..."))
    (^void bondContractDetails [_, ^int reqId ^ContractDetails contractDetails] (println "..."))


    ;; Executions
    ;;(^void execDetails [_, ^int reqId, ^Contract contract, ^Execution execution] (println "..."))

    ;; Market Depth
    (^void updateMktDepth [_, ^int tickerId, ^int position, ^int operation, ^int side, ^double price, ^int size] (println "..."))
    (^void updateMktDepthL2 [_, ^int tickerId, ^int position, ^String marketMaker, ^int operation, ^int side, ^double price, ^int size] (println "..."))

    ;; News Bulletins
    (^void updateNewsBulletin [_, ^int msgId, ^int msgType, ^String message, ^String origExchange] (println "..."))

    ;; Financial Advisors
    (^void managedAccounts [_, ^String accountsList] (println "..."))
    (^void receiveFA [_, ^int faDataType, ^String xml] (println "..."))

    ;; Historical Data
    (^void historicalData [_, ^int reqId, ^String date, ^double open, ^double high, ^double low, ^double close, ^int volume, ^int count, ^double WAP, ^boolean hasGaps] (println "..."))

    ;; Market Scanners
    (^void scannerParameters [_, ^String xml] (println "..."))
    (^void scannerData [_, ^int reqId, ^int rank, ^ContractDetails contractDetails, ^String distance, ^String benchmark, ^String projection, ^String legsStr] (println "..."))

    ;; Real Time Bars
    (^void realtimeBar [_, ^int reqId, ^long time, ^double open, ^double high, ^double low, ^double close, ^long volume, ^double wap, ^int count] (println "..."))

    ;; Fundamental Data
    (^void fundamentalData [_, ^int reqId, ^String data] (println "..."))

    )
  )
