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
            [cemerick.shoreleave.rpc :refer (defremote)]
            [cemerick.shoreleave.rpc :as rpc]))


(defremote heartbeat [arg1 & remaining]

  (log/debug "REMOTE > heartbeat CALLED > arg1[~{arg1}] remaining[~{remaining}]")
  )

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
      rpc/wrap-rpc
      wrap-params
      wrap-multipart-params
      handler/site))

(defn -main []
  (let [port (Integer. (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port :join? false})))
