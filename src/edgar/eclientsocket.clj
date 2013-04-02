(ns edgar.eclientsocket
  (:import  [com.ib.client EClientSocket AnyWrapper]
            [com.interrupt.edgar EWrapperImpl])
  (:require [edgar.ewrapper :as wrapper]))


(defn connect-to-tws []
  
  ;;let [wr (defonce wrap (wrapper/create-ewrapper))
  (let [wr (defonce wrap (EWrapperImpl.))
        es (defonce esocket (EClientSocket. wrap))
        ]
    (.eConnect esocket "localhost" 7497 0)
    { :esocket esocket :ewrapper wrap }
  )
)

(defn disconnect-to-tws []

  (if (.isConnected esocket)
    (.eDisconnect esocket))
)
