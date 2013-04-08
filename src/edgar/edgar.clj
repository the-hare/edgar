(ns edgar.edgar
  (:import  (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution)
            (com.interrupt.edgar IBSpout)
            (backtype.storm StormSubmitter LocalCluster)
            (storm.starter.spout TwitterSampleSpout))
  (:use [clojure.repl]
        [backtype.storm clojure config]
        [clojure.core.strint]
        #_[datomic.api :only [q db] :as d])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [backtype.storm.clojure :as storm]
            #_[edgar.datomic :as edatomic]
            [edgar.eclientsocket :as socket]
            )
  (:gen-class)
  )


;; INTERACTIVE BROKERS code
(defn connect []
  (socket/connect-to-tws))

(defn getMarketData []


  #_(edgar.datomic/database-connect)
  #_@(d/transact edgar.datomic/conn  [{:db/id (d/tempid :db.part/db) :stock/symbol "IBM"}])

  (def connect-result (connect))
  (def contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil))
  (def mdata (.reqMktData edgar.eclientsocket/esocket 0 contract nil false))
  (defonce ibspout2 (IBSpout.))
  (storm/defbolt printstuff ["word"] [tuple collector]
       (println (str "printstuff --> tuple["tuple "] > collector["collector "]")) )

  ;; tie EWrapperImpl to a Spout that I created
  (.setTuple ibspout2 (.getTuple (:ewrapper connect-result))))


;; STORM code
#_(defspout ibspout ["sentence"]
  [conf context collector]
  (let [sentences ["a little brown dog"
                   "the man petted the dog"
                   "four score and seven years ago"
                   "an apple a day keeps the doctor away"]]
    (spout
     (nextTuple []
                (Thread/sleep 100)
                (emit-spout! collector [(rand-nth sentences)]))
     (ack [id]
          ;; You only need to define this method for reliable spouts
          ;; (such as one that reads off of a queue like Kestrel)
          ;; This is an unreliable spout, so it does nothing here
          ))))


#_(defbolt ibbolt ["word"] [tuple collector]

  (let [words (.split (.getString tuple 0) " ")]
    (doseq [w words]
      (emit-bolt! collector [w] :anchor tuple))
    (ack! collector tuple)
  )
)


#_(defn mk-topology [in-spout]
  (storm/topology
   { "1" (storm/spout-spec in-spout) }
   { "3" (storm/bolt-spec  { "1" :shuffle }
                           ibbolt
                           :p 5)
     }))


#_(defn run-wib! []
  (let [connect-result (connect)
        contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil)
        mdata (.reqMktData edgar.eclientsocket/esocket 0 contract nil false)

        xxx (println (str "*** Wrapper CREATION > " edgar.eclientsocket/wrap " ***"))
        my-spout (IBSpout. edgar.eclientsocket/wrap)

        cluster (LocalCluster.)]

    (.submitTopology cluster "ibbolt" {TOPOLOGY-DEBUG true} (mk-topology my-spout))
    (Thread/sleep 10000)
    (.shutdown cluster)))


#_(defn run-twitter! []
  (let [my-spout (TwitterSampleSpout. "twashing@gmail.com" "numd33nut5")
        cluster (LocalCluster.)]

    (.submitTopology cluster "ibbolt" {TOPOLOGY-DEBUG true} (mk-topology my-spout))
    (Thread/sleep 10000)
    (.shutdown cluster)))


(defn -main
  ([]
     (run-twitter!))
  ([ib]
     (run-wib!)))
