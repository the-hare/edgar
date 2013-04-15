(ns edgar.ib.loader

  (:use [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [edgar.ib.market :as market]
            [edgar.splitter :as splitter]
            )
  )

#_(defn get-stock-lists [client]

  (with-open [amexfile (io/reader "etc/amexlist.csv")
              nysefile (io/reader "etc/nyselist.csv")
              nasdaqfile (io/reader "etc/nasdaqlist.csv")
              ]

    (let [amexlist   (csv/read-csv amexfile)
          nyselist   (csv/read-csv nysefile)
          nasdaqlist (csv/read-csv nasdaqfile)]

      #_(reduce (fn [rslt ech]

                (println (<< "calling reqMktData on [~{(-> ech first string/trim)}]"))
                (market/request-market-data client rslt (-> ech first string/trim))
                (inc rslt))
              0
              (doall (take 100 (rest nyselist))))
      )
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

(defn filter-price-movement
  "Run through stocks and filter based on the stocks that have the biggest high / low price movement"
  [client]


  ;; get first 100 stocks
  (let [stock-lists (get-stock-lists)
        first-hundred (take 100 (:nyselist stock-lists))
        after-hundred (nthrest (:nyselist stock-lists) 101)

        bucket-hundred (ref ())
        ]


    (defn- next-bucket-id []
      ;; ... TODO
      )

    ;; subscribe to EWrapper mkt data events
    (defn- snapshot-handler [rst]

      ;; when getting stock data, when results arrive, decide if
      ;;
      ;; i. it's within the top 100 price ranges
      ;; ii. if not, discard, ii.i) get the next ID ii.ii) get the next stock ii.iii) reqMarketData for that next stock
      ;; ... TODO

      ;; (splitter/pushEvent rst)
      (dosync alter @bucket-hundred conj rst)
      (println "snapshot-handler > [" rst "] > bucket-hundred > [" @bucket-hundred "]")
      )

    (market/subscribe-to-market snapshot-handler)

    ;; reqMarketData for those
    (reduce (fn [rslt ech]

              (println (<< "first-hundred reqMktData on [~{(-> ech first string/trim)}]"))
              (market/request-market-data client rslt (-> ech first string/trim) true)
              (inc rslt))
            0
            (doall first-hundred)))

  ;; repeat constantly through: NYSE, NASDAQ, AMEX
  ;; ... TODO
  )


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        ]
    (filter-price-movement client)
    )
  )
