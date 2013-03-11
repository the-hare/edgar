package com.interrupt.edgar;
  
import com.ib.client.EWrapper;
import com.ib.client.EClientSocket;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.ContractDetails;
import com.ib.client.Execution;


/*[datomic.api :only [q db] :as d])
  (:require [edgar.datomic])
*/

public class EWrapperImpl implements com.ib.client.EWrapper {

  // Connection & Server
  public void currentTime (long time) {
    System.out.println(String.format("EWrapper.currentTime > time[%l]", time));
  }
  public void error (int id, int errorCode, String errorString) {
    System.out.println(String.format("EWrapper.error > id[%i] > errorCode[%i] > errorString[%s]", id, errorCode, errorString));
  }
  public void error (Exception error) {
    System.out.println(String.format("EWrapper.error > error[%1]", error));
  }
  public void connectionClosed () {
    System.out.println("EWrapper.connectionClosed");
  }
  public void tickSnapshotEnd (int arg) {
    System.out.println(String.format("EWrapper.tickSnapshotEnd > arg[%i]", arg));
  } 

  // Market Data
  public void tickPrice (int tickerId, int field, double price, int canAutoExecute) {
    
    System.out.println("");
    System.out.println("EWrapper.tickPrice");
    switch(field) {
      case 1:
        System.out.println(String.format("case 1 > bid-price[%d]", price));
      case 2:
        System.out.println(String.format("case 2 > ask-price[%d]", price));
      case 4:
        System.out.println(String.format("case 4 > last-price[%d]", price));
      case 6:
        System.out.println(String.format("case 6 > high[%d]", price));
      case 7:
        System.out.println(String.format("case 7 > low[%d]", price));
      case 9:
        System.out.println(String.format("case 9 > close[%d]", price));
      default:
        System.out.println("default > noop");
    }
    /*(let [stock {:db/id (d/tempid :db.part/db) :stock/symbol "IBM" }
          mstock (merge stock { (case field 1 :stock/bid-price 2 :stock/ask-price 4 :stock/last-price 6 :stock/high 7 :stock/low 9 :stock/close) price })
          add-result @(d/transact edgar.datomic/conn [mstock])
          ]

      (println (<< "EWrapper.tickPrice CALLED > tickerId[~{tickerId}] > field[~{field}] > price[~{price}] > canAutoExecute[~{canAutoExecute}] > db.transact[~{add-result} / ~{mstock}]"))
    )
    */
  }
  
  
  public void tickSize (int tickerId, int field, int size) {
  
    System.out.println(String.format("EWrapper.tickSize > tickerId[%i] > field[%i] > size[%i]", tickerId, field, size));
  }

  public void tickOptionComputation (int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
  
    System.out.println(
      String.format("EWrapper.tickOptionComputation > tickerId[%i] > field[%i] > impliedVol[%d] > delta[%d] > optPrice[%d] > pvDividend[%d] > gamma[%d] > vega[%d] > theta[%d] > undPrice[%d]", 
        tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice));
  }

  public void tickGeneric (int tickerId, int tickType, double value) {
  
    System.out.println(String.format("EWrapper.tickGeneric > tickerId[%i] > tickType[%i] > value[%d]", tickerId, tickType, value));
  }
  
  public void tickString (int tickerId, int tickType, String value) {
    
    System.out.println(String.format("EWrapper.tickString > tickerId[%i] > tickType[%i] > value[%d]", tickerId, tickType, value));
  }
  
  public void tickEFP ( int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, 
                        int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
  
    System.out.println(
      String.format("EWrapper.tickEFP > tickerId[%i], tickType[%i], basisPoints[%d], formattedBasisPoints[%s], impliedFuture[%d], holdDays[%i], futureExpiry[%s], dividendImpact[%d], dividendsToExpiry[%d]", 
        tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureExpiry, dividendImpact, dividendsToExpiry));
  }

}



  /*
    ;; Orders
    (^void orderStatus [_, ^int orderId, ^String status, ^int filled, ^int remaining, ^double avgFillPrice, ^int permId, ^int parentId, ^double lastFillPrice, ^int clientId, ^String whyHeld] (println "..."))
    (^void openOrder [_, ^int orderId, ^Contract contract, ^Order order, ^OrderState orderState] (println "..."))
    (^void nextValidId [_, ^int orderId] (println "..."))


    ;; Account & Portfolio
    (^void updateAccountValue [_, ^String key, ^String value, ^String currency, ^String accountName] (println "..."))
    (^void updatePortfolio [_, ^Contract contract, ^int position, ^double marketPrice, ^double marketValue, ^double averageCost, ^double unrealizedPNL, ^double realizedPNL, ^String accountName] (println "..."))
    (^void updateAccountTime [_, ^String timeStamp] (println "..."))


    ;; Contract Details
    (^void contractDetails [_, ^int reqId, ^ContractDetails contractDetails]
      (println (<< "EWrapper.contractDetails CALLED > this[~{_}] > reqId[~{reqId}] > contractDetails[~{contractDetails}]")))
    (^void contractDetailsEnd [_, ^int reqId]
      (println (<< "EWrapper.contractDetailsEnd CALLED > this[~{_}] > reqId[~{reqId}]")))
    (^void bondContractDetails [_, ^int reqId ^ContractDetails contractDetails]
      (println (<< "EWrapper.bondContractDetails CALLED > this[~{_}] > reqId[~{reqId}] > contractDetails[~{contractDetails}]")))


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
    (^void historicalData [_, ^int reqId, ^String date, ^double open, ^double high, ^double low, ^double close, ^int volume, ^int count, ^double WAP, ^boolean hasGaps]
      (println (<< "EWrapper.historicalData CALLED > this[~{_}] > reqId[~{reqId}], date[~{date}], open[~{open}], high[~{high}], low[~{low}], close[~{close}], volume[~{volume}], count[~{count}], WAP[~{WAP}], hasGaps[~{hasGaps}]")))

    ;; Market Scanners
    (^void scannerParameters [_, ^String xml] (println (<< "EWrapper.scannerParameters CALLED > this[~{_}] > xml[~{xml}]")))
    (^void scannerData [_, ^int reqId, ^int rank, ^ContractDetails contractDetails, ^String distance, ^String benchmark, ^String projection, ^String legsStr]
      (println (<< "EWrapper.historicalData CALLED > this[~{_}] > reqId[~{reqId}], rank[~{rank}], contractDetails[~{contractDetails}], distance[~{distance}], benchmark[~{benchmark}], projection[~{projection}], legsStr[~{legsStr}]")))

    ;; Real Time Bars
    (^void realtimeBar [_, ^int reqId, ^long time, ^double open, ^double high, ^double low, ^double close, ^long volume, ^double wap, ^int count] (println "..."))

    ;; Fundamental Data
    (^void fundamentalData [_, ^int reqId, ^String data] (println "..."))
  */


