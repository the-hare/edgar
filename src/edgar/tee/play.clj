(ns edgar.tee.play
  (:require [clojure.tools.logging :as log]
            [edgar.datomic :as edatomic]))


(defn tee-historical
  ""
  [tick]

  (log/debug "tee.datomic/tee-market > tick[" tick "]"))


(defn tee-market
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
 [tick-list tick]


 ;; transform tick structure
 (let [final-tick {:historical/symbol (:symbol tick)
                   :historical/company (:company tick)
                   :historical/price-difference (:price-difference tick)

                   :historical/high (-> tick :event-list first (get "high" 0.0))
                   :historical/low (-> tick :event-list first (get "low" 0.0))
                   :historical/WAP (-> tick :event-list first (get "WAP" 0.0))

                   :historical/open (-> tick :event-list first (get "open" 0.0))
                   :historical/close (-> tick :event-list first (get "close" 0.0))

                   :historical/date (-> tick :event-list first (get "date" ""))
                   :historical/count (-> tick :event-list first (get "count" 0))
                   :historical/hasGaps (-> tick :event-list first (get "hasGaps" false))
                   :historical/volume (-> tick :event-list first (get "volume" 0))
                   }
       ]

   (println "tee.play/tee-market > final-tick[" final-tick "] / tick-list[" (count tick-list) "][" tick-list "]")

   ))
