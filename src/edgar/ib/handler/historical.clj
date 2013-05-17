(ns edgar.ib.handler.historical

  (:use [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [edgar.ib.market :as market]
            [edgar.scheduler :as scheduler]
            [edgar.tee.datomic :as tdatomic]
            ))

(defn- insert-into-event-list
  "bucket-tranche will have a structure of: [ { :id rslt :symbol stock-sym :event-list [] } ]
   i) go into the bucket, ii) find the appropriate element and iii) insert event into the :event-list"
  [bucket-tranche event-index rst]


  ;; weed out historicalData events that are finished
  (dosync (alter bucket-tranche
                 (fn [blist]

                   ;;(log/debug "insert-into-event-list > event-index[" event-index "] > blist > type[" (type blist) "] > data[" blist "]")
                   (update-in blist
                              [ event-index :event-list ]
                              (fn [inp]
                                (conj inp rst))))))
)

(defn- insert-price-difference
  "insert price difference, iff type is 'historicalData'"
  [bucket-tranche event-index rst]

  (if (and (= "historicalData" (rst "type"))
             (-> (rst "high") nil? not)
             (-> (rst "low") nil? not))

      ;; find this iteration's price difference
      (let [price-difference (- (rst "high")
                                (rst "low"))
            prev-difference (:price-difference (nth @bucket-tranche event-index))]

        ;; determine if greater than the existing price-difference
        (if (and (-> prev-difference nil? not)
                 (> price-difference prev-difference))

          (dosync (alter bucket-tranche
                         update-in
                         [ event-index ]
                         (fn [inp]

                           ;;(log/debug "insert-price-difference > event-index[" event-index "] > blist > type[" (type inp) "] > data[" inp "]")
                           (merge inp { :price-difference price-difference })) ))))))

(defn- order-by-price-difference
  "Order list by largest price difference"
  [bucket-tranche]
  (dosync (alter bucket-tranche
                   (fn [inp]

                     ;;(log/debug "order-by-price-difference > input data[" inp "]")
                     (into []   ;; put the result into a vector
                           (sort-by :price-difference > inp)))
                   )))


(defn- local-request-market-data [options]

  (let [bucket (:bucket options)
        rslt (:id options)
        stock-sym (:stock-symbol options)
        stock-name (:stock-name options)
        client (:client options)]

    (dosync (alter bucket conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] } ))
    (market/request-market-data client rslt stock-sym true))
  )

(defn- local-request-historical-data [options]

  (let [bucket (:bucket options)
        rslt (:id options)
        stock-sym (:stock-symbol options)
        stock-name (:stock-name options)
        client (:client options)]

    ;;(log/debug "local-request-historical-data > options[" options "]")

    (dosync (alter bucket conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] :processed? false } ))
    (market/request-historical-data client rslt stock-sym))
  )


(defn- handle-snapshot-end [options rst]

  (let [bucket (:bucket options)
        client (:client options)
        bsize (:bucket-size options)
        stock-lists (:stock-lists options)
        ]


    ;; ii.i get the next ID - (rst "tickerId")
    ;; ii.ii) get the next stock
    ;;(log/debug "snapshot-handler > SNAPSHOT END result [" rst "] > bucket-tranche [" bucket "] > stock-lists SIZE [" (count stock-lists) "]")


    ;; remove previous stock & mktRequest for next stock
    (let [rid (rst "tickerId")
          ]

      ;; a marker to know when this element has been processed
      (dosync (alter bucket (fn [inp]
                              (into []
                                    (for [x inp
                                          :when #(= rid (:id %))]
                                      (merge x {:processed? true}))
                                    )
                              )))

      (market/cancel-market-data client rid)


      ;; push to Tee / Datomic; Data structure looks like:
      (if (every? #(:processed? %) @bucket)
        (tdatomic/tee-historical (:conn options) @bucket))

      ))
    )

(defn- handle-snapshot-continue [options rst]

  ;; when getting stock data, when results arrive, decide if
  ;;
  ;; i. it's within the top 100 price ranges
  ;; ii. if not, discard,



  (let [bucket (:bucket options)
        client (:client options)
        bsize (:bucket-size options)
        stock-lists (:stock-lists options)
        event-index (-> (filter (fn [inp] (= (:id (second inp))
                                            (rst "tickerId") ))
                                (map-indexed vector @bucket) )
                        ffirst)
        ]

    ;;(log/debug "handle-snapshot-continue > event-index[" event-index "][" (-> event-index nil? not) "]  > rst[" rst "] > options[" (dissoc options :stock-lists) "]")
    (if (-> event-index nil? not)

      (do

        (insert-into-event-list bucket event-index rst)
        (insert-price-difference bucket event-index rst)

        #_(order-by-price-difference bucket))))
  )


;; subscribe to EWrapper mkt data events
(defn snapshot-handler [options rst]


  (log/debug "")
  (log/debug "")
  (log/debug "snapshot-handler > event index [" nil #_event-index "] result [" rst "] > bucket [" nil #_@bucket "]")

  (if (and (= "historicalData" (rst "type"))
           (re-find #"finished-" (rst "date")))

    ;; ***
    ;; snapshot END
    (handle-snapshot-end options rst)

    ;; ***
    ;; otherwise process events
    (handle-snapshot-continue options rst))

  )

(defn schedule-historical-data
  "Request historical data in configured tranches. Interactive Brokers allows at most, 60 simultaneous requests every 10 minutes

   Options are:
     :bucket - the container into which result stock events are put
     :tranche-size - for each 10 minute request, the amount of stocks for which we are requesting historical data
     :remaining-list - the entire list of stocks for which we are requesting historical data"

  [options]

  (let [bucket (:bucket options)
        tranche-size (if (:tranche-size options) (:tranche-size options) 10)
        remaining-list (ref (:stock-lists options))
        ]

    (scheduler/initialize-pool)
    (scheduler/schedule-task
     (merge {:min 1} (:scheduler-options options))
     (fn []

       (let [current-tranche (take tranche-size @remaining-list)]

         (log/debug "")
         (log/debug "schedule-historical-data > RUNNING task > remaining-list count[" (count @remaining-list)"] current-tranche[" current-tranche "]")

         ;; ii.iii) reqMarketData for that next stock; repeat constantly through: NYSE, NASDAQ, AMEX
         (dosync (alter bucket (fn [inp] [] )))

         ;; A. Iterate through tranche and make a historical data request
         (reduce (fn [rslt ech]

                   (let [stock-sym (-> ech first string/trim)
                         stock-name (-> ech second string/trim)]

                     (local-request-historical-data (dissoc
                                                     (merge {:id rslt :stock-symbol stock-sym :stock-name stock-name} options)
                                                     :stock-lists)))
                   (inc rslt))
                 0
                 current-tranche
                 )

         ;; B. ensure that remaining list is decremented
         (dosync (alter remaining-list nthrest tranche-size)))
       )
     ))
  )
