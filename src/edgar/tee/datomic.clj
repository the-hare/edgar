(ns edgar.tee.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [clojure.tools.logging :as log]
            [edgar.tee.tee :as tns]
            [edgar.datomic :as edatomic]))

(defn tee-historical
 "Process the list of entities. First, flatten out the :event-list, and merge it into the entity

 [{:id 0,
   :symbol DDD,
   :company 3D Systems Corporation,
   :price-difference 1.3100000000000023,
   :event-list [{high 35.11,
                 tickerId 0,
                 WAP 34.491,
                 open 35.07,
                 date 20130426,
                 count 3403,
                 low 33.8,
                 hasGaps false,
                 close 34.53,
                 field historicalData,
                 volume 8667,
                 type historicalData}]}]"
 [conn bucket]


 ;; collect all data into a transaction list, then persist
 (let [final-tx (reduce (fn [rslt ech]

                          (conj rslt
                                {:db/id (d/tempid :db.part/user)
                                 :historical/symbol (:symbol ech)
                                 :historical/company (:company ech)
                                 :historical/price-difference (:price-difference ech)

                                 :historical/high (-> ech :event-list first (get "high" 0.0))
                                 :historical/low (-> ech :event-list first (get "low" 0.0))
                                 :historical/WAP (-> ech :event-list first (get "WAP" 0.0))

                                 :historical/open (-> ech :event-list first (get "open" 0.0))
                                 :historical/close (-> ech :event-list first (get "close" 0.0))

                                 :historical/date (-> ech :event-list first (get "date" ""))
                                 :historical/count (-> ech :event-list first (get "count" 0))
                                 :historical/hasGaps (-> ech :event-list first (get "hasGaps" false))
                                 :historical/volume (-> ech :event-list first (get "volume" 0))
                                 })
                          )
                        []
                        bucket)
       ]

   (log/debug "tee.datomic/tee-historical > final-tx[" final-tx "]")
   (d/transact conn final-tx)
   )
 )

(defn tee-market
  ""
  [tick-list]

  (log/info "tee.datomic/tee-market > tick[" tick-list "]"))
