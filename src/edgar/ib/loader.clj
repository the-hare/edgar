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
            [edgar.ib.handler.historical :as historical]
            [edgar.splitter :as splitter]
            [edgar.scheduler :as scheduler]
            [edgar.datomic :as edatomic]
            [edgar.tee.datomic :as tdatomic]
            ))

(defn- get-stock-lists []

  (let [amexfile (io/reader "etc/amexlist.csv")
        nysefile (io/reader "etc/nyselist.csv")
        nasdaqfile (io/reader "etc/nasdaqlist.csv")]

    {:amexlist   (csv/read-csv amexfile)
     :nyselist   (csv/read-csv nysefile)
     :nasdaqlist (csv/read-csv nasdaqfile)
     }
    ))

(defn- get-concatenated-stock-lists []

  (with-open [amexfile (io/reader "etc/amexlist.csv")
              nysefile (io/reader "etc/nyselist.csv")
              nasdaqfile (io/reader "etc/nasdaqlist.csv")]

    (doall (concat
            (rest (csv/read-csv nysefile))
            (rest (csv/read-csv nasdaqfile))
            (rest (csv/read-csv amexfile))))
    )
  )


(defn filter-price-movement
  "Run through stocks and filter based on the stocks that have the biggest high / low price movement"
  [client conn]


  ;; get first tranch of stocks
  (let [bucket (ref [])
        stock-lists (get-concatenated-stock-lists)
        options {:bucket bucket
                 :client client
                 :tee-list [(partial tdatomic/tee-historical conn)]
                 :stock-lists stock-lists
                 :tranche-size 60
                 :scheduler-options {:min 10.5}}
        ]


    (log/debug "filter-price-movement > bucket[" bucket "] > stock-list-size[" (count stock-lists) "]")

    (market/subscribe-to-market (partial historical/snapshot-handler options))
    (historical/schedule-historical-data options)
    ))


(defn test-run []

  (let [client (:esocket (market/connect-to-market))
        conn (edatomic/database-connect nil)]
    (filter-price-movement client conn)
    ))

(defn stub-run []
  (test-run))
