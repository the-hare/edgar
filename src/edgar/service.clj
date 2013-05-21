(ns edgar.service
  (:use [clojure.tools.namespace.repl])
  (:require [edgar.datomic :as edatomic]
            [edgar.core.edgar :as edgar]
            [edgar.ib.handler.historical :as historical]
            [edgar.ib.handler.live :as live]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
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
        result (live/load-filtered-results 20 conn)]
    (ring-resp/response result)))



;;
(defn resume-historical [context result-map]

  (log/info "... resume-historical > result-map[" #_result-map "] / context[" context "]")
  (iimpl/resume

   ;;(assoc :session {:ib-client (:client result-map)})
   (-> context
       (assoc :request (merge (:request context) {:session {:ib-client (:client result-map)}}))  ;; put a session in the request
       (assoc :response (ring-resp/response (:result result-map))))))

(defn async-historical [paused-context]

     (let [client (or (-> paused-context :request :session :ib-client)
                   (:interactive-brokers-client (edgar/initialize-workbench)))
           stock-selection [ (-> paused-context :request :query-params :stock-selection) ]
           time-duration (-> paused-context :request :query-params :time-duration)
           time-interval (-> paused-context :request :query-params :time-interval)
           ]

       (log/info (str "... async-historical 1 > stock-selection[" stock-selection "] > time-duration[" time-duration "] > time-interval[" time-interval "] > client-from-session[" (nil? (:interactive-brokers-client (edgar/initialize-workbench))) "]"))

       (edgar/play-historical
        client
        stock-selection
        time-duration
        time-interval
        [(fn [tick-list]

           (log/info (str "... async-historical 2 > result[" tick-list "] / resume-fn[" (:resume-fn paused-context) "]"))
           ((:resume-fn paused-context) {:result tick-list :client client}))])
       ))

(defbefore get-historical-data
  "Get historical data for a particular stock"
  [{request :request :as context}]

  ;;(def *BIG_CONTEXT* context)
  ;;(log/info (str "... get-historical-data > REQUEST[" request "]"))
  ;;(log/info (str "... get-historical-data > CONTEXT[" context "]"))
  (iimpl/with-pause [paused-context context]
    (async-historical
        (assoc paused-context :resume-fn (partial resume-historical paused-context)))))



;;
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
