(ns edgar.ib.loader

  (:use [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [clojure.pprint :as pprint]
            [edgar.ib.market :as market]
            [edgar.splitter :as splitter]
            )
  )

(defn get-stock-lists []

  (let [amexfile (io/reader "etc/amexlist.csv")
        nysefile (io/reader "etc/nyselist.csv")
        nasdaqfile (io/reader "etc/nasdaqlist.csv")
        ]
    {:amexlist   (csv/read-csv amexfile)
     :nyselist   (csv/read-csv nysefile)
     :nasdaqlist (csv/read-csv nasdaqfile)
     }
    ))


#_(defn- next-bucket-id []
    ;; ... TODO
    )

(defn- insert-into-event-list
  "bucket-hundred will have a structure of: [ { :id rslt :symbol stock-sym :event-list [] } ]
   i) go into the bucket, ii) find the appropriate element and iii) insert event into the :event-list"
  [bucket-hundred event-index rst]

  ;; weed out historicalData events that are finished
  (if (and (= "historicalData" (rst "type"))
           (re-find #"finished-" (rst "date")))

    ()  ;; noop
    (dosync (alter bucket-hundred
                   (fn [blist]
                     (update-in blist
                                [ event-index :event-list ]
                                (fn [inp]
                                  (conj inp rst))))))
    )
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
                     (into []   ;; put the result into a vector
                           (sort-by :price-difference > inp)))
                   )))


;; subscribe to EWrapper mkt data events
(defn- snapshot-handler [bucket-hundred rst]


  ;; (splitter/pushEvent rst)

  (let [event-index (first (first
                            (filter (fn [inp] (= (:id (second inp))
                                                (rst "tickerId") ))
                                    (map-indexed vector @bucket-hundred) )))
        ]

    (println "snapshot-handler > event index [" event-index "] > result [" rst "] > bucket-hundred [" @bucket-hundred "]")


    (insert-into-event-list bucket-hundred event-index rst)
    (insert-price-difference bucket-hundred event-index rst)
    (order-by-price-difference bucket-hundred)

    (pprint/pprint @bucket-hundred)


    ;; when getting stock data, when results arrive, decide if
    ;;
    ;; i. it's within the top 100 price ranges
    ;; ii. if not, discard,

    ;; ii.i get the next ID

    ;; ii.ii) get the next stock

    ;; ii.iii) reqMarketData for that next stock


    ;; ... TODO
    ;; check if bucket-hundred has reached the 100 threshold
    ;; if yes... go through and find daily high & lows; calculate the difference
    ;; keep the largest 100 differences, and discard the rest


    )

  )

(defn filter-price-movement
  "Run through stocks and filter based on the stocks that have the biggest high / low price movement"
  [client]


  ;; get first 100 stocks
  (let [bucket-hundred (ref [])
        stock-lists (get-stock-lists)
        first-hundred (take 1 (rest (:nyselist stock-lists)))
        after-hundred (nthrest (rest (:nyselist stock-lists)) 101)
        ]


    (market/subscribe-to-market (partial snapshot-handler bucket-hundred))

    ;; reqMarketData for first 100 stocks
    (reduce (fn [rslt ech]

              (let [stock-sym (-> ech first string/trim)
                    stock-name (-> ech second string/trim)]

                (println (<< "first-hundred reqMktData on [~{stock-sym}]"))
                (dosync (alter bucket-hundred conj { :id rslt :symbol stock-sym :company stock-name :price-difference 0.0 :event-list [] } ))
                (market/request-market-data client rslt stock-sym true)

                (inc rslt)
                ))
            0
            (doall first-hundred))


    (println "BUCKET-100 > " @bucket-hundred))

  ;; repeat constantly through: NYSE, NASDAQ, AMEX
  ;; ... TODO
  )


(defn test-run []

  (let [client (:esocket (market/connect-to-market))]
    (filter-price-movement client)
    )
  )
(defn xxx []
  (test-run))
