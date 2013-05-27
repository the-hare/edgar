(ns edgar.service
  (:import [javax.servlet.http HttpServletRequest HttpServletResponse])
  (:require [edgar.core.edgar :as edgar]
            [edgar.ib.market :as market]
            [edgar.datomic :as edatomic]
            [edgar.ib.handler.live :as live]

            [clojure.java.io :as io]
            [io.pedestal.service.log :as log]
            [io.pedestal.service.http :as bootstrap]
            [io.pedestal.service.http.route :as route]
            [io.pedestal.service.http.sse :as sse]
            [io.pedestal.service.http.body-params :as body-params]
            [io.pedestal.service.http.route.definition :refer [defroutes]]
            [io.pedestal.service.http.ring-middlewares :as middlewares]
            [io.pedestal.service.http.impl.servlet-interceptor :as servlet-interceptor]
            [io.pedestal.service.impl.interceptor :as iimpl]
            [io.pedestal.service.interceptor :as interceptor :refer [defon-response defbefore defafter]]
            [io.pedestal.service.interceptor :refer [defhandler definterceptor]]
            [ring.middleware.session.memory :as rmemory]
            [ring.middleware.session.cookie :as rcookie]
            [ring.util.response :as ring-resp]
            ))



;; HOME Page
(defhandler home-page
  [request]

  (-> (ring-resp/response (slurp (io/resource "include/index.html")))
      (ring-resp/content-type "text/html")))


;; LIST Filtered Stocks
(defhandler list-filtered-input
  "List high-moving stocks"
  [request]

  (let [conn (edatomic/database-connect nil)
        result (live/load-filtered-results 20 conn)]
    (ring-resp/response result)))




;; HISTORICAL Data
(defn resume-historical [context result-map]

  (let [response-result (ring-resp/response result-map)]

    (log/info (str "... resume-historical > paused-context class[" (class context) "] > response [" (class response-result) "] [" response-result "]"))
    (iimpl/resume
     (-> context
         (assoc :response response-result)))))

(defn async-historical [paused-context]

  (let [client (:interactive-brokers-client (edgar/initialize-workbench))
        stock-selection [ (-> paused-context :request :query-params :stock-selection) ]
        time-duration (-> paused-context :request :query-params :time-duration)
        time-interval (-> paused-context :request :query-params :time-interval)]

    (log/info (str "... async-historical 1 > paused-context class["
                   (class paused-context) "] > stock-selection["
                   stock-selection "] > time-duration["
                   time-duration "] > time-interval["
                   time-interval "] > client-from-session["
                   (:session (:request paused-context)) "]"))

    (market/create-event-channel)
    (edgar/play-historical client stock-selection time-duration time-interval [(fn [tick-list]

                                                                                 (market/close-market-channel)

                                                                                 (let [final-list (reduce (fn [rslt ech]
                                                                                                            (conj rslt [(ech "date") (ech "close")]))
                                                                                                          []
                                                                                                          (-> tick-list first :event-list))]

                                                                                   ((:resume-fn paused-context) {:stock-list final-list :stock-name (-> tick-list first :company)})))])))
(defbefore get-historical-data
  "Get historical data for a particular stock"
  [{request :request :as context}]

  (iimpl/with-pause [paused-context context]
    (async-historical
        (assoc paused-context :resume-fn (partial resume-historical paused-context)))))




;; LIVE Data
(def stored-streaming-context (atom nil))

(defn init-streaming-stock-data [sse-context]
  (log/info (str "... init-streaming-stock-data CALLED > sse-context[" sse-context "]"))
  (reset! stored-streaming-context sse-context))

(defn stop-streaming-stock-data
  "Stops streaming stock data for 1 or a list of stocks"
  []
  (when-let [streaming-context @stored-streaming-context]
    (reset! stored-streaming-context nil)
    (sse/end-event-stream streaming-context)))


(defn stream-live [event-name result]

  (log/info (str "... stream-live > context[" streaming-context "] > event-name[" event-name "] response[" result "]"))

  (try
    (sse/send-event @stored-streaming-context event-name (pr-str result))
    (catch java.io.IOException ioe
      (stop-streaming-stock-data))))

(defn get-streaming-stock-data [request]

  (println (str "... get-streaming-stock-data CALLED > servlet-response[" (:servlet-response request) "] > sse-context[" @stored-streaming-context "]"))
  (let [client (:interactive-brokers-client edgar/*interactive-brokers-workbench*)
        stock-selection [ (-> request :query-params :stock-selection) ]
        stock-name (-> request :query-params :stock-name)]

    #_(edgar/play-live client stock-selection [(fn [tick-list tick]

                                               (let [final-list (reduce (fn [rslt ech]
                                                                          (conj rslt [(:last-trade-time ech) (:last-trade-price ech)]))
                                                                        []
                                                                        tick-list)]

                                                 (stream-live "stream-live" {:stock-list final-list :stock-name stock-name})))])
    (Thread/sleep 5000)
    (stream-live "stream-live" {:stock-list [[1368215573010 203.98] [1368215576331 203.99] [1368215576857 203.99] [1368215577765 203.99] [1368215578769 204.0] [1368215579272 204.01] [1368215579517 204.02] [1368215581769 204.02] [1368215583602 204.01] [1368215585650 204.02] [1368215586060 204.02] [1368215587029 204.01] [1368215588318 204.02] [1368215589335 204.01] [1368215589536 204.01] [1368215589846 204.0] [1368215591079 203.99] [1368215591789 203.99] [1368215592104 203.98] [1368215592615 203.98] [1368215592758 203.99] [1368215594039 203.97] [1368215597119 203.98] [1368215597632 203.97] [1368215599396 203.97] [1368215603876 203.96] [1368215606059 203.96] [1368215610316 203.95] [1368215610634 203.95] [1368215610813 203.93] [1368215612886 203.95] [1368215615858 203.94] [1368215618621 203.94] [1368215619138 203.96] [1368215623846 203.94] [1368215632669 203.94] [1368215634709 203.92] [1368215636587 203.93] [1368215636952 203.94] [1368215638328 203.93]]
                                :stock-name stock-name})
    (-> ""
        ring-resp/response
        (ring-resp/content-type "text/event-stream"))))


(definterceptor session-interceptor
  (middlewares/session {:store (rcookie/cookie-store)} ))

(defroutes routes
  [[["/" {:get home-page}

     ^:interceptors [body-params/body-params, session-interceptor]
     ["/list-filtered-input" {:get list-filtered-input}]
     ["/get-historical-data" {:get get-historical-data}]
     ["/get-streaming-stock-data" { :get [::init-streaming-stock-data (sse/start-event-stream init-streaming-stock-data)]
                                    :post get-streaming-stock-data}]
     ]]])

;; You can use this fn or a per-request fn via io.pedestal.service.http.route/url-for
(def url-for (route/url-for-routes routes))

;; Consumed by sseve.server/create-server
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
