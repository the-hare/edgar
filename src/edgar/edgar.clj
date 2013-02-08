(ns edgar.edgar
  (:import (com.ib.client EWrapper EClientSocket))
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
    (orderStatus [orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld] (println "..."))
    (openOrder [orderId, Contract contract, Order order, OrderState orderState] (println "..."))
    (nextValidId [orderId] (println "..."))


    ;; Account & Portfolio
    (updateAccountValue [key, value, currency, accountName] (println "..."))
    (updateAccountTime [contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName] (println "..."))
    (updatePortfolio [timeStamp] (println "..."))


    ;; Contract Details
    (contractDetails [ReqId, ContractDetails contractDetails] (println "..."))
    (contractDetailsEnd [reqId] (println "..."))
    (bondContractDetails [reqId, ContractDetails contractDetails] (println "..."))


    ;; Executions
    (exectDetails [reqId, Contract contract, Execution execution] (println "..."))

    ;; Market Depth
    (updateMktDepth [tickerId, position, operation, side, price, size] (println "..."))
    (updateMktDepthL2 [tickerId, position, marketMaker, operation, side, price, size] (println "..."))

    ;; News Bulletins
    (updateNewsBulletin [msgId, msgType, message, origExchange] (println "..."))

    ;; Financial Advisors
    (managedAccounts [accountsList] (println "..."))
    (receiveFA [faDataType, string xml] (println "..."))

    ;; Historical Data
    (historicalData [reqId, date, open, high, low, close, volume, count, WAP, boolean hasGaps] (println "..."))

    ;; Market Scanners
    (scannerParameters [xml] (println "..."))
    (scannerData [reqId, rank, ContractDetails contractDetails, distance, benchmark, projection, legsStr] (println "..."))

    ;; Real Time Bars
    (realTimeBar [reqId, time, open, high, low, close, volume, wap, count] (println "..."))

    ;; Fundamental Data
    (fundamentalData [reqId, data] (println "..."))

    )
  )
