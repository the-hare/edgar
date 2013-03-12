package com.interrupt.edgar;

import com.ib.client.EWrapper;
import com.ib.client.EClientSocket;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.ContractDetails;
import com.ib.client.Execution;


public class Edgar extends EWrapperImpl {
  
  
  private String symbol = null;
  private int requestId = 0;
  private double lastPrice = 0.0;
  
  public Edgar(String symbol) {
    this.symbol = symbol;
  }
  
  public void run() {
    
    connectToTWS();
    Contract contract = createContract(symbol, "STK", "SMART", "USD");
    client.reqMktData(requestId++, contract, null, true);
  }
  public static void main(String[] args) {
     
    if (args.length != 1) {
      System.out.println("Usage: java Edgar <symbol>");
      System.exit(1);
    }
    else {
      new Edgar(args[0]).start();
    }
  }
}


  /*
        [datomic.api :only [q db] :as d])
  (:require [edgar.eclientsocket :as socket]
            [edgar.ewrapper :as ewrapper]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [edgar.datomic :as edatomic]
            )
  )
  */


/*
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
  (edgar.datomic/database-connect)

  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData client 0 contract nil false))

  #_@(d/transact edgar.datomic/conn  [{:db/id (d/tempid :db.part/db) :stock/symbol "IBM"}])
)
(defn getStockLists []

  (with-open [amexfile (io/reader "etc/amexlist.csv")
              nysefile (io/reader "etc/nyselist.csv")
              nasdaqfile (io/reader "etc/nasdaqlist.csv")
              ]

    (let [amexlist   (csv/read-csv amexfile)
          nyselist   (csv/read-csv nysefile)
          nasdaqlist (csv/read-csv nasdaqfile)]

      (reduce (fn [rslt ech]

                (println (<< "calling reqMktData on [~{(-> ech first string/trim)}]"))
                (.reqMktData client rslt (Contract. rslt, (-> ech first string/trim), "STK", nil, 0.0, nil, nil, "SMART", "USD", nil, nil, nil, false, nil, nil) nil true)
                (inc rslt))
              0
              (doall (take 50 (rest nyselist))))
      )
    )
  )
*/

