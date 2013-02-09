(ns edgar.edgar
  (:import (com.ib.client EWrapper EClientSocket Contract Order OrderState))
  )


(defn create-ewwrapper
  "When reifying Java interface methods, we need the 'this' arg and I find you need to hint the exact types when reifying java interfaces"
  []

  (reify EWrapper


    ;; Connection & Server
    ;;(currentTime [time] (println "..."))
    ;;(error [id, errorCode, errorString] (println "..."))
    ;;(error [error] (println "..."))
    (connectionClosed [_] (println "..."))


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
    (^void contractDetails [_, ReqId, ContractDetails contractDetails] (println "..."))
    (^void contractDetailsEnd [_, reqId] (println "..."))
    (^void bondContractDetails [_, reqId, ContractDetails contractDetails] (println "..."))


    ;; Executions
    (^void exectDetails [_, reqId, Contract contract, Execution execution] (println "..."))

    ;; Market Depth
    (^void updateMktDepth [_, tickerId, position, operation, side, price, size] (println "..."))
    (^void updateMktDepthL2 [_, tickerId, position, marketMaker, operation, side, price, size] (println "..."))

    ;; News Bulletins
    (^void updateNewsBulletin [_, msgId, msgType, message, origExchange] (println "..."))

    ;; Financial Advisors
    (^void managedAccounts [_, accountsList] (println "..."))
    (^void receiveFA [_, faDataType, string xml] (println "..."))

    ;; Historical Data
    (^void historicalData [_, reqId, date, open, high, low, close, volume, count, WAP, boolean hasGaps] (println "..."))

    ;; Market Scanners
    (^void scannerParameters [_, xml] (println "..."))
    (^void scannerData [_, reqId, rank, ContractDetails contractDetails, distance, benchmark, projection, legsStr] (println "..."))

    ;; Real Time Bars
    (^void realTimeBar [_, reqId, time, open, high, low, close, volume, wap, count] (println "..."))

    ;; Fundamental Data
    (^void fundamentalData [_, reqId, data] (println "..."))

    )
  )
