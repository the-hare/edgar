(ns edgar.edgar
  (:import (com.ib.client EWrapper EClientSocket))
  )


(defn create-ewwrapper []

  (reify EWrapper


    ;; Connection & Server
    (currentTime [] (println "..."))
    (error [] (println "..."))
    (connectionClosed [] (println "..."))


    ;; Market Data
    (tickPrice [tickerId, field, double price, canAutoExecute] (println "..."))
    (tickSize [tickerId, field, size] (println "..."))
    (tickOptionComputation [tickerId, field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice] (println "..."))
    (tickGeneric [tickerId, tickType, double value] (println "..."))
    (tickString [tickerId, tickType, String value] (println "..."))
    (tickEFP [tickerId, tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry] (println "..."))


    ;; Orders
    (orderStatus [] (println "..."))
    (openOrder [] (println "..."))
    (nextValidId [] (println "..."))


    ;; Account & Portfolio
    (updateAccountValue [] (println "..."))
    (updateAccountTime [] (println "..."))
    (updatePortfolio [] (println "..."))


    ;; Contract Details
    (contractDetails [] (println "..."))
    (contractDetailsEnd [] (println "..."))
    (bondContractDetails [] (println "..."))
    (exectDetails [] (println "..."))


    ;; Market Depth
    (updateMktDepth [] (println "..."))
    (updateMktDepthL2 [] (println "..."))

    ;; News Bulletins
    (updateNewsBulletin [] (println "..."))

    ;; Financial Advisors
    (managedAccounts [] (println "..."))
    (receiveFA [] (println "..."))

    ;; Historical Data
    (historicalData [] (println "..."))

    ;; Market Scanners
    (scannerParameters [] (println "..."))
    (scannerData [] (println "..."))

    ;; Real Time Bars
    (realTimeBar [] (println "..."))

    ;; Fundamental Data
    (fundamentalData [] (println "..."))

    )
  )
