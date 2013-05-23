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
            [io.pedestal.service.http.sse :as sse]
            [io.pedestal.service.interceptor :refer [defhandler definterceptor]]
            [io.pedestal.service.impl.interceptor :as iimpl]
            [io.pedestal.service.interceptor :as interceptor :refer [defon-response defbefore defafter]]
            [lamina.core :as lamina]
            [ring.middleware.session :as rsession]
            [ring.middleware.session.memory :as rmemory]
            [ring.middleware.session.cookie :as rcookie]
            [ring.util.response :as ring-resp]))


;; HOME Page
(defhandler home-page
  [request]

  (->
   (ring-resp/response (slurp (io/resource "include/index.html")))
   (ring-resp/content-type "text/html")))



;; Listing Filtered Stocks
(defhandler list-filtered-input
  "List high-moving stocks"
  [request]

  (let [conn (edatomic/database-connect nil)
        result (live/load-filtered-results 20 conn)]
    (ring-resp/response result)))



;; HISTORICAL Data
(defn resume-historical [context result-map]

  (let [response-result (ring-resp/response (:result result-map))
        response-final (if (-> response-result :request :session :ib-client) ;; conditionally put the IB client into session
                         response-result
                         (merge response-result {:session {:ib-client (:client result-map)}}))]

    (log/info (str "... resume-historical > paused-context class[" (class context) "] > response [" (class response-final) "] [" response-final "]"))
    (iimpl/resume
     (-> context
         (assoc :response response-final)))))
(defn async-historical [paused-context]

     (let [client (or (-> paused-context :request :session :ib-client)
                      (:interactive-brokers-client (edgar/initialize-workbench)))
           stock-selection [ (-> paused-context :request :query-params :stock-selection) ]
           time-duration (-> paused-context :request :query-params :time-duration)
           time-interval (-> paused-context :request :query-params :time-interval)]

       (log/info (str "... async-historical 1 > paused-context class["
                      (class paused-context) "] > stock-selection["
                      stock-selection "] > time-duration["
                      time-duration "] > time-interval["
                      time-interval "] > client-from-session["
                      (:session (:request paused-context)) "]"))

       #_(edgar/play-historical client stock-selection time-duration time-interval [(fn [tick-list]
                                                                                      ((:resume-fn paused-context) {:result tick-list :client client}))])
       ((:resume-fn paused-context) {:result [1 2 3 4] :client client})
       ))
(defbefore get-historical-data
  "Get historical data for a particular stock"
  [{request :request :as context}]

  (iimpl/with-pause [paused-context context]
    (async-historical
        (assoc paused-context :resume-fn (partial resume-historical paused-context)))))



;; LIVE Data
(def stored-streaming-context (atom nil))
(defn store-streaming-context [streaming-context]
  (reset! stored-streaming-context streaming-context))
(defn stop-streaming-stock-data
  "Stops streaming stock data for 1 or a list of stocks"
  []
  (when-let [streaming-context @stored-streaming-context]
    (reset! stored-streaming-context nil)
    (sse/end-event-stream streaming-context)))


(defn stream-live
  [result]

  (log/info (str "... stream-live > response[" (:result result) "]"))
  (when-let [streaming-context @stored-streaming-context]
    (try
      (sse/send-event streaming-context "stream-live" result)
      (catch java.io.IOException ioe
        (stop-streaming-stock-data)))))
(defn get-streaming-stock-data
  "Get streaming stock data for 1 or a list of stocks"
  [streaming-context]

  (let [client (or (-> streaming-context :session :ib-client)
                   (:interactive-brokers-client (edgar/initialize-workbench)))
        stock-selection [ (-> streaming-context :query-params :stock-selection)]

        event-channel (ref (lamina/channel))
        subscribe-fn (fn [handle-fn]
                       (lamina/receive-all @event-channel handle-fn))
        publish-fn (fn [event]
                     (lamina/enqueue @event-channel event))
        ]

    (log/info (str "... get-streaming-stock-data > client[" client "] > stock-selection[" stock-selection "] > streaming-context[" streaming-context "]"))

    (subscribe-fn stream-live)
    (edgar/play-live client stock-selection [(fn [tick-list]

                                               (publish-fn tick-list)
                                               #_(stream-live "stream-live" {:result tick-list :client client}))])))

#_(defn get-streaming-stock-data
  [streaming-context]

  (when-let [streaming-context @stored-streaming-context]
    (try
      (sse/send-event streaming-context "stream-live" "qwerty")
      (catch java.io.IOException ioe
        (stop-streaming-stock-data)))))


(definterceptor session-interceptor
  (middlewares/session {:store (rmemory/memory-store)}))
(defroutes routes
  [[
    ["/" {:get home-page}

     ;; Set default interceptors for /about and any other paths under /
     ;; ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ^:interceptors [#_middlewares/params
                     #_middlewares/keyword-params
                     session-interceptor]

     ["/list-filtered-input" {:get list-filtered-input}]
     ["/get-historical-data" {:get get-historical-data}]

     ["/init-streaming-stock-data" {:get [::edgar (sse/start-event-stream store-streaming-context)]}]
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
              ;;::bootstrap/type :tomcat
              ::bootstrap/port 8080})
