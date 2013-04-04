(ns edgar.edgar
  (:import  (com.ib.client EWrapper EClientSocket Contract Order OrderState ContractDetails Execution)
            (com.interrupt.edgar IBSpout)
            (backtype.storm StormSubmitter LocalCluster))
  (:use [clojure.repl]
        [backtype.storm clojure config]
        [clojure.core.strint]
        #_[datomic.api :only [q db] :as d])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [backtype.storm.clojure :as storm]
            #_[edgar.datomic :as edatomic]
            #_[edgar.eclientsocket :as socket]
            #_[edgar.ewrapper :as ewrapper]
            )
  (:gen-class)
  )

(defspout ibspout ["sentence"]
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


(defbolt ibbolt ["word"] [tuple collector]

  (let [words (.split (.getString tuple 0) " ")]
    (doseq [w words]
      (emit-bolt! collector [w] :anchor tuple))
    (ack! collector tuple)
  )
)


(defn mk-topology []
  (storm/topology
   { "1" (storm/spout-spec ibspout) }
   { "3" (storm/bolt-spec  { "1" :shuffle }
                           ibbolt
                           :p 5)
     }))


(defn run-local! []
  (let [cluster (LocalCluster.)]
    (.submitTopology cluster "ibbolt" {TOPOLOGY-DEBUG true} (mk-topology))
    (Thread/sleep 10000)
    (.shutdown cluster)))


(defn -main
  ([]
     (run-local!)) )


