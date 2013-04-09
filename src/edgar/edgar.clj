(ns edgar.edgar
  (:import  (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution)
            #_(com.interrupt.edgar IBSpout)
            #_(backtype.storm StormSubmitter LocalCluster)
            #_(storm.starter.spout TwitterSampleSpout))
  (:use [clojure.repl]
        [clojure.core.strint]
        #_[backtype.storm clojure config]
        #_[datomic.api :only [q db] :as d])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            #_[backtype.storm.clojure :as storm]
            #_[edgar.datomic :as edatomic]
            [edgar.eclientsocket :as socket]
            )
  (:gen-class)
  )


;; INTERACTIVE BROKERS code
(defn connect []
  (socket/connect-to-tws))

(defn trial-run []


  #_(edgar.datomic/database-connect)
  #_@(d/transact edgar.datomic/conn  [{:db/id (d/tempid :db.part/db) :stock/symbol "IBM"}])

  (def connect-result (connect))

  ;; 1. Go through all stocks and find those with biggest daily price difference
  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData edgar.eclientsocket/esocket 0 contract nil false))


  ;; 1.1 listen to EWRAPPER



  ;; 2. determine if we are already watching this stock


  ;; 3. if not listening, then add to pool


  ;; tie EWrapperImpl to a Spout that I created
  #_(.setTuple ibspout2 (.getTuple (:ewrapper connect-result))))


(defn get-market-data []

  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData edgar.eclientsocket/esocket 0 contract nil false))

  )
