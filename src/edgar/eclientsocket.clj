(ns edgar.eclientsocket
  (:import [com.ib.client EClientSocket AnyWrapper])
  (:require [edgar.ewrapper :as wrapper]))


(defn connect-to-tws []

  (let [wr (defonce wrap (wrapper/create-ewrapper))
        es (defonce esocket (EClientSocket. wrap))
        ]
    (.eConnect esocket "localhost" 7497 0)
    esocket
  )
)

(defn disconnect-to-tws []

  (if (.isConnected esocket)
    (.eDisconnect esocket))
)
