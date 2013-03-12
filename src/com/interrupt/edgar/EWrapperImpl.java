package com.interrupt.edgar;

import com.ib.client.EWrapper;
import com.ib.client.EClientSocket;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.ContractDetails;
import com.ib.client.Execution;
import com.ib.client.CommissionReport;
import com.ib.client.UnderComp;


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
    System.out.println(String.format("EWrapper.error(String) > error[%1]", error));
  }
  public void error (String error) {
    System.out.println(String.format("EWrapper.error[Exception] > error[%1]", error));
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



  // Orders
  public void orderStatus ( int orderId, String status, int filled, int remaining, double avgFillPrice, int permId,
                            int parentId, double lastFillPrice, int clientId, String whyHeld) {
    System.out.println("...");
  }
  public void openOrder (int orderId, Contract contract, Order order, OrderState orderState) {
    System.out.println("...");
  }
  public void nextValidId (int orderId) {
    System.out.println("...");
  }


  // Account & Portfolio
  public void updateAccountValue (String key, String value, String currency, String accountName) {
    System.out.println("...");
  }
  public void updatePortfolio (Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
    System.out.println("...");
  }
  public void updateAccountTime (String timeStamp) {
    System.out.println("...");
  }


  // Contract Details
  public void contractDetails (int reqId, ContractDetails contractDetails) {
    System.out.println(String.format("EWrapper.contractDetails > reqId[%i] > contractDetails[%2]", reqId, contractDetails));
  }
  public void contractDetailsEnd (int reqId) {
    System.out.println(String.format("EWrapper.contractDetailsEnd > reqId[%i]", reqId));
  }
  public void bondContractDetails (int reqId, ContractDetails contractDetails) {
    System.out.println(String.format("EWrapper.bondContractDetails > reqId[%i] > contractDetails[%2]", reqId, contractDetails));
  }


  // Executions
  public void execDetails (int reqId, Contract contract, Execution execution) { System.out.println("..."); }

  // Market Depth
  public void updateMktDepth (int tickerId, int position, int operation, int side, double price, int size) {
    System.out.println("...");
  }
  public void updateMktDepthL2 (int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
    System.out.println("...");
  }

  // News Bulletins
  public void updateNewsBulletin (int msgId, int msgType, String message, String origExchange) {
    System.out.println("...");
  }

  // Financial Advisors
  public void managedAccounts (String accountsList) {
    System.out.println("...");
  }
  public void receiveFA (int faDataType, String xml) {
    System.out.println("...");
  }

  // Historical Data
  public void historicalData (int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
    System.out.println(
      String.format("EWrapper.historicalData > reqId[%i] > date[%s] > open[%d] > high[%d] > low[%d] > close[%d] > volume[%i] > count[%i] > WAP[%d] > hasGaps[%b]",
        reqId, date, open, high, low, close, volume, count, WAP, hasGaps));
  }

  // Market Scanners
  public void scannerParameters (String xml) {
    System.out.println(String.format("EWrapper.scannerParameters > xml[%s]", xml));
  }
  public void scannerData (int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
    System.out.println(
      String.format("EWrapper.scannerData > reqId[%i] > rank[%i] > contractDetails[%3] > distance[%s] > benchmark[%s] > projection[%s] > legalStr[%s]",
       reqId, rank, contractDetails, distance, benchmark, projection, legsStr));
  }


  // Real Time Bars
  public void realtimeBar (int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
    System.out.println("...");
  }

  // Fundamental Data
  public void fundamentalData (int reqId, String data) {
    System.out.println("...");
  }

  // et al.
  public void openOrderEnd() { System.out.println("..."); }
  public void accountDownloadEnd(java.lang.String arg1) { System.out.println("..."); }
  public void execDetailsEnd(int arg1) { System.out.println("..."); }
  public void scannerDataEnd(int arg1) { System.out.println("..."); }
  public void deltaNeutralValidation(int arg1, com.ib.client.UnderComp arg2) { System.out.println("..."); }
  public void marketDataType(int arg1, int arg2) { System.out.println("..."); }
  public void commissionReport(com.ib.client.CommissionReport commisionReport) { System.out.println("..."); }

}
