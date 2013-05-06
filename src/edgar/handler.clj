(ns edgar.handler
  (:gen-class)
  (:import [java.io File])
  (:use [clojure.core.strint]
        [compojure.core]
        [ring.middleware.params]
        [ring.middleware.multipart-params]
        [ring.adapter.jetty])
  (:require [clojure.tools.logging :as log]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring-response]
            [clojure.java.io :as io]
            [shoreleave.middleware.rpc :as rpc]
            ))


(rpc/defremote ^{:remote-name :heartbeat} remote-fn [args]

  (log/debug "REMOTE > heartbeat CALLED > args[~{args}]")
  "thing")


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
