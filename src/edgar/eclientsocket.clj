(ns edgar.eclientsocket
  (:import [com.ib.client EClientSocket AnyWrapper])
  (:require [edgar.ewrapper :as wrapper]))


(defn connect-to-tws []

  (let [wr (defonce wrap (wrapper/create-ewrapper))
        es (defonce esocket (EClientSocket. wrap))
        ]
    (.eConnect esocket "localhost", 7469, 1)
  )
)

(defn disconnect-to-tws []

  (if (.isConnected esocket)
    (.eDisconnect esocket))
)
