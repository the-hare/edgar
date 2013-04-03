(ns edgar.edgar
  (:import  (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution)
            (com.interrupt.edgar IBSpout))
  (:use [clojure.repl]
        [clojure.core.strint]
        [datomic.api :only [q db] :as d])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [backtype.storm.clojure :as storm]
            [edgar.datomic :as edatomic]
            [edgar.eclientsocket :as socket]
            [edgar.ewrapper :as ewrapper]
            )
  )


(defn connect []
  (socket/connect-to-tws)
)
(defn getMarketData []


  #_(edgar.datomic/database-connect)
  #_@(d/transact edgar.datomic/conn  [{:db/id (d/tempid :db.part/db) :stock/symbol "IBM"}])

  (def connect-result (connect))
  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData (:esocket connect-result) 0 contract nil false))
  (defonce ibspout (IBSpout.))

  ;; tie EWrapperImpl to a Spout that I created
  (.setTuple ibspout (.getTuple (:ewrapper connect-result)))


  (storm/defbolt printstuff ["word"] [tuple collector]
    (println (str "printstuff --> tuple["tuple"] > collector["collector"]"))
  )
  (storm/topology
   { "1" (storm/spout-spec ibspout)
   }
   { "3" (storm/bolt-spec  { "1" :shuffle }
                           printstuff
         )
   })

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
                #_(.reqMktData client rslt (Contract. rslt, (-> ech first string/trim), "STK", nil, 0.0, nil, nil, "SMART", "USD", nil, nil, nil, false, nil, nil) nil true)
                (inc rslt))
              0
              (doall (take 50 (rest nyselist))))
      )
    )
  )
