(ns edgar.handler
  (:gen-class)
  (:import [java.io File])
  (:use [compojure.core]
        [ring.middleware.params]
        [ring.middleware.multipart-params]
        [ring.adapter.jetty])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as ring-response]
            [clojure.java.io :as io]))

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
      handler/site))

(defn -main []
  (let [port (Integer. (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port :join? false})))
