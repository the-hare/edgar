(ns edgar.tee.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [clojure.tools.logging :as log]
            [edgar.tee.tee :as tns]
            [edgar.datomic :as edatomic]))

(defn tee
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
 [bucket]

 ;; collect all data into a transaction list, then persist
 (let [final-tx (reduce (fn [rslt ech]
                          (conj rslt
                                {:historical/symbol (:symbol ech)
                                 :historical/company (:company ech)
                                 :historical/price-difference (:price-difference ech)

                                 :historical/high (-> ech :event-list first "high")
                                 :historical/low (-> ech :event-list first "low")
                                 :historical/WAP (-> ech :event-list "WAP")

                                 :historical/open (-> ech :event-list first "open")
                                 :historical/close (-> ech :event-list first "close")

                                 :historical/date (-> ech :event-list first "date")
                                 :historical/count (-> ech :event-list first "count")
                                 :historical/hasGaps (-> ech :event-list first "hasGaps")
                                 :historical/volume (-> ech :event-list first "volume")
                                 })
                          )
                        []
                        bucket)
       ]

   (log/debug "tee.datomic/tee > final-tx[" final-tx "]")
   @(d/transact conn final-tx)
   )
 )
