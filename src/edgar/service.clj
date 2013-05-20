(ns edgar.service
  (:use [clojure.tools.namespace.repl])
  (:require [edgar.datomic :as edatomic]
            [edgar.core.edgar :as edgar]
            [edgar.ib.handler.historical :as historical]
            [edgar.ib.handler.live :as live]
            [clojure.java.io :as io]
            [io.pedestal.service.http :as bootstrap]
            [io.pedestal.service.http.route :as route]
            [io.pedestal.service.http.body-params :as body-params]
            [io.pedestal.service.http.route.definition :refer [defroutes]]
            [io.pedestal.service.http.ring-middlewares :as middlewares]
            [io.pedestal.service.interceptor :refer [defhandler definterceptor]]
            [io.pedestal.service.impl.interceptor :as iimpl]
            [io.pedestal.service.interceptor :as interceptor :refer [defon-response defbefore defafter]]
            [ring.middleware.session :as rsession]
            [ring.middleware.session.memory :as rmemory]
            [ring.util.response :as ring-resp]))


;;
(defhandler home-page
  [request]

  (->
   (ring-resp/response (slurp (io/resource "include/index.html")))
   (ring-resp/content-type "text/html")))


;;
(defhandler list-filtered-input
  "List high-moving stocks"
  [request]

  (let [conn (edatomic/database-connect nil)
        result (live/load-filtered-results nil conn)]
    (ring-resp/response result)))

(defhandler get-historical-data
  "Get historical data for a particular stock"
  [request]

  ;; ... TODO: setup IB-client
  ;; ... TODO: Pass in: i) stock selection, ii) time-duration, iii) itme-interval
  ;; ... TODO: make asynchronous

  (let [client (or (-> request :session :ib-client)
                   (:interactive-brokers-client (edgar/initialize-workbench)))
        stock-selection ["IBM" "APPL"]
        time-duration "300 S"
        time-interval "1 secs"]

    (println "... client[" client "]")
    (edgar/play-historical client stock-selection time-duration time-interval)

    (-> (ring-resp/response "get-historical-data")
        (assoc :session {:ib-client client}))))

(defn get-streaming-stock-data
  "Get streaming stock data for 1 or a list of stocks"
  [request]
  (ring-resp/response "get-streaming-stock-data"))

(defn stop-streaming-stock-data
  "Stops streaming stock data for 1 or a list of stocks"
  [request]
  (ring-resp/response "stop-streaming-stock-data"))


(definterceptor session-interceptor
  (middlewares/session {:store (rmemory/memory-store)}))

(defroutes routes
  [[
    ["/" {:get home-page}

     ;; Set default interceptors for /about and any other paths under /
     ;; ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ^:interceptors [middlewares/params
                     middlewares/keyword-params
                     session-interceptor]

     ["/list-filtered-input" {:get list-filtered-input}]
     ["/get-historical-data" {:get get-historical-data}]
     ["/get-streaming-stock-data" {:get get-streaming-stock-data}]
     ["/stop-streaming-stock-data" {:post stop-streaming-stock-data}]]
    ]])


;; You can use this fn or a per-request fn via io.pedestal.service.http.route/url-for
(def url-for (route/url-for-routes routes))

;; Consumed by edgar.server/create-server
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; :bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::boostrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"
              ::bootstrap/file-path "/public"

              ;; Either :jetty or :tomcat (see comments in project.clj
              ;; to enable Tomcat)
              ;;::bootstrap/host "localhost"
              ::bootstrap/type :jetty
              ::bootstrap/port 8080})
