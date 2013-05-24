(ns edgar.tee.play
  (:require [clojure.tools.logging :as log]
            [edgar.datomic :as edatomic]))


(defn tee-historical
  "Incoming event structure will look like the following:

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
  [tick-list]

  (log/info "tee.play/tee-historical > tick-list[" (count tick-list) "][" tick-list "]"))


(defn tee-market
 "Process the list of entities. First, flatten out the :event-list, and merge it into the entity"
 [tick-list tick]


 (log/info "tee.play/tee-market > tick[" tick "] / tick-list[" (count tick-list) "][" tick-list "]"))
