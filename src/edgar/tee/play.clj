(ns edgar.tee.play
  (:require [clojure.tools.logging :as log]
            [edgar.datomic :as edatomic]))


(defn tee-historical
  ""
  [tick-list]

  (println "tee.play/tee-historical > tick-list[" (count tick-list) "][" tick-list "]")

  )


(defn tee-market
 "Process the list of entities. First, flatten out the :event-list, and merge it into the entity"
 [tick-list tick]


 (println "tee.play/tee-market > tick[" tick "] / tick-list[" (count tick-list) "][" tick-list "]")


 ;; historical play at sub-second intervals
 ;; ... fix log/debug

 ;; create signals for each filter

 ;; create strategy, which invokes a signal, based on:
 ;;   the price against a filter
 ;;   wrap a publish / subscribe model around signals

 )
