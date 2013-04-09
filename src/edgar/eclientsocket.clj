(ns edgar.eclientsocket
  (:import  [com.ib.client EClientSocket AnyWrapper]
            [com.interrupt.edgar EWrapperImpl])
  #_(:require [edgar.ewrapper :as wrapper])
  )


(defn connect-to-tws []

  (let [wr (defonce EWRAPPER (ref (EWrapperImpl.)))
        es (defonce esocket (EClientSocket. @EWRAPPER))
        ]
    (.eConnect esocket "localhost" 7497 0)
    { :esocket esocket :ewrapper @EWRAPPER }
  )
)

(defn disconnect-to-tws []

  (if (.isConnected esocket)
    (.eDisconnect esocket))
)
