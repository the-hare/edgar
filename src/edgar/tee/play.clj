(ns edgar.tee.play
  (:require [clojure.tools.logging :as log]
            [edgar.datomic :as edatomic]))


(defn tee-historical
  ""
  [tick]

  (log/debug "tee.datomic/tee-market > tick[" tick "]"))


(defn tee-market
 "Process the list of entities. First, flatten out the :event-list, and merge it into the entity"
 [tick-list tick]


 (println "tee.play/tee-market > tick[" tick "] / tick-list[" (count tick-list) "][" tick-list "]")


 ;; make sure we are recieving time data

 ;; do historical play

 ;; create signals for each filter

 ;; create strategy, which invokes a signal, based on:
 ;;   the price against a filter
 ;;   wrap a publish / subscribe model around signals

 )
