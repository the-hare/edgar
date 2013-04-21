(ns edgar.ib.loader

  (:use [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [clojure.pprint :as pprint]
            [clojure.tools.logging :as log]
            [edgar.ib.market :as market]
            [edgar.splitter :as splitter]
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
  "bucket-hundred will have a structure of: [ { :id rslt :symbol stock-sym :event-list [] } ]
   i) go into the bucket, ii) find the appropriate element and iii) insert event into the :event-list"
  [bucket-hundred event-index rst]


  ;; weed out historicalData events that are finished
  (dosync (alter bucket-hundred
                 (fn [blist]

                   (log/debug "insert-into-event-list > event-index[" event-index "] > blist > type[" (type blist) "] > data[" blist "]")
                   (update-in blist
                              [ event-index :event-list ]
                              (fn [inp]
                                (conj inp rst))))))
)

(defn- insert-price-difference
  "insert price difference, iff type is 'historicalData'"
  [bucket-hundred event-index rst]

  (if (and (= "historicalData" (rst "type"))
             (-> (rst "high") nil? not)
             (-> (rst "low") nil? not))

      ;; find this iteration's price difference
      (let [price-difference (- (rst "high")
                                (rst "low"))
            prev-difference (:price-difference (nth @bucket-hundred event-index))]

        ;; determine if greater than the existing price-difference
        (if (and (-> prev-difference nil? not)
                 (> price-difference prev-difference))

          (dosync (alter bucket-hundred
                         update-in
                         [ event-index ]
                         (fn [inp]
                           (merge inp { :price-difference price-difference })) ))))))

(defn- order-by-price-difference
  "Order list by largest price difference"
  [bucket-hundred]
  (dosync (alter bucket-hundred
                   (fn [inp]

                     (log/debug "order-by-price-difference > input data[" inp "]")
                     (into []   ;; put the result into a vector
                           (sort-by :price-difference > inp)))
                   )))


(defn- local-request-market-data [options]

  (let [bucket (:bucket-hundred options)
        rslt (:id options)
        stock-sym (:stock-symbol options)
        stock-name (:stock-name options)
        client (:client options)]

    (dosync (alter bucket conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] } ))
    (market/request-market-data client rslt stock-sym true))
  )


;; subscribe to EWrapper mkt data events
(defn- snapshot-handler [options rst]


  ;; (splitter/pushEvent rst)

  (let [bucket (:bucket options)
        client (:client options)
        bsize (:bucket-size options)
        stock-lists (:stock-lists options)
        ]


    (if (= "tickSnapshotEnd" (rst "type"))

      ;; ***
      ;; snapshot END
      (do

        ;; ii.i get the next ID - (rst "tickerId")
        ;; ii.ii) get the next stock

        (log/debug "snapshot-handler > SNAPSHOT END result [" rst "] > bucket-hundred [" @bucket "] > stock-lists [" @stock-lists "]")

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

              (dosync (alter bucket (fn [inp] (into [] (take (- bsize 1) inp)))  ))
              (dosync (alter stock-lists rest))

              (if (< 0 (count @stock-lists))  ;; only go until there are no more stocks to process

                (let [stock-sym (-> @stock-lists first first string/trim)
                      stock-name (-> @stock-lists first second string/trim)]

                  (local-request-market-data {:bucket-hundred bucket
                                              :id next-id
                                              :stock-symbol stock-sym
                                              :stock-name stock-name
                                              :client client}))))
            ))

        )

      ;; ***
      ;; otherwise process events
      (if (and (= "historicalData" (rst "type"))
           (re-find #"finished-" (rst "date")))

        () ;; noop
        (do

          ;; when getting stock data, when results arrive, decide if
          ;;
          ;; i. it's within the top 100 price ranges
          ;; ii. if not, discard,

          (let [temp-index (-> (filter (fn [inp] (= (:id (second inp))
                                                   (rst "tickerId") ))
                                       (map-indexed vector @bucket) )
                               ffirst)
                event-index (if (nil? temp-index) (count @bucket) temp-index)
                ]

            (log/debug "")
            (log/debug "")
            (log/debug "snapshot-handler > event index [" event-index "] result [" rst "] > bucket-hundred [" @bucket "]")

            (insert-into-event-list bucket event-index rst)
            (insert-price-difference bucket event-index rst)
            (order-by-price-difference bucket))

          )))
    )
  )

(defn filter-price-movement
  "Run through stocks and filter based on the stocks that have the biggest high / low price movement"
  [client]


  ;; get first 100 stocks
  (let [bucket-hundred (ref [])
        stock-lists (get-concatenated-stock-lists)
        bsize 2

        first-hundred (take bsize (rest stock-lists))
        remaining (ref (take bsize (-> stock-lists rest rest rest)))
        ]


    (log/debug "filter-price-movement > BUCKET[" bucket-hundred "] > REMAINING[" remaining "]")
    (market/subscribe-to-market (partial snapshot-handler {:bucket bucket-hundred :client client :bucket-size bsize :stock-lists remaining}))

    ;; reqMarketData for first 100 stocks
    (reduce (fn [rslt ech]

              (let [stock-sym (-> ech first string/trim)
                    stock-name (-> ech second string/trim)]

                (local-request-market-data {:bucket-hundred bucket-hundred
                                            :id rslt
                                            :stock-symbol stock-sym
                                            :stock-name stock-name
                                            :client client
                                            })

                (inc rslt)
                ))
            0
            (doall first-hundred))
    )

  )


(defn test-run []

  (let [client (:esocket (market/connect-to-market))]
    (filter-price-movement client)
    )
  )
(defn xxx []
  (test-run)
  )
