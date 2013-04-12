(ns edgar.ib.loader

  (:use [clojure.core.strint])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [edgar.ib.market :as market]
            )
  )

(defn get-stock-lists [client]

  (with-open [amexfile (io/reader "etc/amexlist.csv")
              nysefile (io/reader "etc/nyselist.csv")
              nasdaqfile (io/reader "etc/nasdaqlist.csv")
              ]

    (let [amexlist   (csv/read-csv amexfile)
          nyselist   (csv/read-csv nysefile)
          nasdaqlist (csv/read-csv nasdaqfile)]

      (reduce (fn [rslt ech]

                (println (<< "calling reqMktData on [~{(-> ech first string/trim)}]"))
                (market/request-market-data client rslt (-> ech first string/trim))
                (inc rslt))
              0
              (doall (take 100 (rest nyselist))))
      )
    )
  )

(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        ]
    (get-stock-lists client)
    )
  )
