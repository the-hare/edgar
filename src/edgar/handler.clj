(ns edgar.handler
  (:gen-class)
  (:import [java.io File])
  (:use [clojure.core.strint]
        [compojure.core]
        [ring.middleware.params]
        [ring.middleware.multipart-params]
        [ring.adapter.jetty])
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring-response]
            [shoreleave.middleware.rpc :as rpc]
            [aleph.http :as aleph]
            [edgar.ib.market :as market]
            ))


(rpc/defremote ^{:remote-name :heartbeat} remote-fn [args]

  (log/debug "REMOTE > heartbeat CALLED > args[" args "]")
  {:resp "thing"})

(rpc/defremote ^{:remote-name :historical-data} historical-data [stock-sym duration-str bar-size]

  (log/debug "REMOTE > historical-data CALLED > stock-sym[" stock-sym "] > duration-str[" duration-str "] > bar-size[" bar-size "]")
  (defn handler-fn [evt]

    ;; ... spit result out to some response channel
    )

  ;; ... create an IB client , idx
  (market/request-historical-data client idx stock-sym duration-str bar-size "TRADES"))


;; [lamina.core.queue :as q]


(defroutes app-routes
  "define the routes that will comprise the application"

  (GET "/" []
       (ring-response/file-response "include/index.html" { :root "public"}))

  ;; ======
  ;; Resource Routes
  (route/files "/" { :root "public"})
  (route/resources "/" { :root "public"})
  (route/not-found "Not Found")
  )

(def app
  "Create the Compojure app"
  (-> app-routes
      wrap-params
      wrap-multipart-params
      rpc/wrap-rpc
      handler/site
      ))

(defn -main []
  (let [port (Integer. (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port :join? false})))
