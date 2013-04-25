(ns edgar.ib.loader

  (:use [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [clojure.pprint :as pprint]
            [clojure.tools.logging :as log]
            [overtone.at-at :as at]

            [edgar.ib.market :as market]
            [edgar.splitter :as splitter]
            [edgar.scheduler :as scheduler]
            )
  )

(defn- get-stock-lists []

  (let [amexfile (io/reader "etc/amexlist.csv")
        nysefile (io/reader "etc/nyselist.csv")
        nasdaqfile (io/reader "etc/nasdaqlist.csv")
        ]
    {:amexlist   (csv/read-csv amexfile)
     :nyselist   (csv/read-csv nysefile)
     :nasdaqlist (csv/read-csv nasdaqfile)
     }
    ))

(defn- get-concatenated-stock-lists []

  (with-open [amexfile (io/reader "etc/amexlist.csv")
              nysefile (io/reader "etc/nyselist.csv")
              nasdaqfile (io/reader "etc/nasdaqlist.csv")
              ]

    (doall (concat
            (rest (csv/read-csv nysefile))
            (rest (csv/read-csv nasdaqfile))
            (rest (csv/read-csv amexfile))))
    )
  )

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

  (let [bucket (:bucket-tranche options)
        rslt (:id options)
        stock-sym (:stock-symbol options)
        stock-name (:stock-name options)
        client (:client options)]

    (dosync (alter bucket conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] } ))
    (market/request-market-data client rslt stock-sym true))
  )

(defn- local-request-historical-data [options]

  (let [bucket (:bucket-tranche options)
        rslt (:id options)
        stock-sym (:stock-symbol options)
        stock-name (:stock-name options)
        client (:client options)]

    (dosync (alter bucket conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] } ))
    (market/request-historical-data client rslt stock-sym))
  )

(defn- schedule-historical-data [options]

  (let [tranche-size 10

        remaining-list (:stock-lists options)
        current-tranche (take tranche-size @remaining-list)
        ]

    (scheduler/initialize-pool)
    (scheduler/schedule-task
     {:sec 1}
     (fn []


       (log/debug "schedule-historical-data > RUNNING task > remaining-list count[" (count @remaining-list)"] current-tranche[" current-tranche "]")

       ;; A. Iterate through tranche and make a historical data request
       (reduce (fn [rslt ech]

                 ;;(log/debug "... reduce idx[" rslt "] > ech[" ech "]")
                 #_(let [stock-sym (-> ech first string/trim)
                         stock-name (-> ech second string/trim)]

                     (local-request-historical-data (extend {:id rslt :stock-symbol stock-sym :stock-name stock-name} options)))
                 (inc rslt)
                 )
               0
               current-tranche
               )

       ;; B. ensure that remaining list is decremented
       (dosync (alter remaining-list (fn [inp] (nthrest tranche-size inp))))

       )
     ))
  )

(defn- handle-snapshot-end [options rst]

  (let [bucket (:bucket options)
        client (:client options)
        bsize (:bucket-size options)
        stock-lists (:stock-lists options)
        ]


    ;; ii.i get the next ID - (rst "tickerId")
    ;; ii.ii) get the next stock
    (log/debug "snapshot-handler > SNAPSHOT END result [" rst "] > bucket-tranche [" nil #_@bucket "] > stock-lists SIZE [" (count @stock-lists) "]")


    ;; remove previous stock & mktRequest for next stock
    (let [rid (rst "tickerId")
          temp-id (first (for [[a b] (partition 2 (sort (for [x @bucket] (:id x))))    ;; run through list and find first gap in IDs
                               :when (not= (+ 1 a) b)]
                           (+ 1 a)))
          next-id (if (nil? temp-id)
                    (+ 1 (last (sort (for [x @bucket] (:id x)))))
                    temp-id)
          ]

      ;; remove only if i) we are at 100 and ii) this is the lowest price difference
      (if (>= (count @bucket) bsize)

        (do

          ;; ii.iii) reqMarketData for that next stock; repeat constantly through: NYSE, NASDAQ, AMEX
          (dosync (alter bucket (fn [inp] (into [] (take (- bsize 1) inp))) ))
          (dosync (alter stock-lists rest))

          (if (< 0 (count @stock-lists))  ;; only go until there are no more stocks to process

            (let [stock-sym (string/trim (first (first @stock-lists )))
                  stock-name (string/trim (second (first @stock-lists )))
                  ]

              (market/cancel-market-data client next-id)
              #_(local-request-historical-data {:bucket-tranche bucket
                                                :id next-id
                                                :stock-symbol stock-sym
                                                :stock-name stock-name
                                                :client client}))

            ;; ** otherwise, we are DONE
            (do
              (pprint/pprint "*** FINISHED Ordering stocks")
              (pprint/pprint @bucket))

            ))
        )))
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

    (if (-> event-index nil? not)

      (do
        (log/debug "")
        (log/debug "")
        (log/debug "snapshot-handler > event index [" event-index "] result [" rst "] > bucket-tranche [" nil #_@bucket "]")

        (insert-into-event-list bucket event-index rst)
        (insert-price-difference bucket event-index rst)
        (order-by-price-difference bucket))))
  )


;; subscribe to EWrapper mkt data events
(defn- snapshot-handler [options rst]


  ;; (splitter/pushEvent rst)

  (if (and (= "historicalData" (rst "type"))
           (re-find #"finished-" (rst "date")))

    ;; ***
    ;; snapshot END
    (handle-snapshot-end options rst)

    ;; ***
    ;; otherwise process events
    (handle-snapshot-continue options rst))

  )

(defn filter-price-movement
  "Run through stocks and filter based on the stocks that have the biggest high / low price movement"
  [client]


  ;; get first tranch of stocks
  (let [bucket-tranche (ref [])
        stock-lists (ref (get-concatenated-stock-lists))
        options {:bucket bucket-tranche :client client :stock-lists stock-lists}
        ]


    (log/debug "filter-price-movement > bucket[" bucket-tranche "] > stock-list-size[" (count @stock-lists) "]")

    (market/subscribe-to-market (partial snapshot-handler options))
    (schedule-historical-data options)

    )
  )


(defn test-run []
  (let [client (:esocket (market/connect-to-market))]
    (filter-price-movement client)
    ))
(defn stub-run []
  (test-run))
