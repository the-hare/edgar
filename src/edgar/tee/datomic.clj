(ns edgar.tee.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [edgar.tee.tee :as tns]))

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

 (map (fn [inp]

        (let [pass-one (merge inp (-> inp :event-list first))
              pass-two (dissoc pass-one :event-list)]

          ;; submit seed data transaction
          @(d/transact conn pass-two)

          )))
 )
