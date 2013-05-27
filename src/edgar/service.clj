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

       (market/create-event-channel)
       (edgar/play-historical client stock-selection time-duration time-interval [(fn [tick-list]

                                                                                    (market/close-market-channel)
                                                                                    ((:resume-fn paused-context) {:result tick-list :client client}))])
    ))
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

    (edgar/play-live client stock-selection [(fn [tick-list tick]

                                               (let [final-list (reduce (fn [rslt ech]
                                                                          (conj rslt [(:last-trade-time ech) (:last-trade-price ech)]))
                                                                        []
                                                                        tick-list)]

                                                 (stream-live "stream-live" {:stock-list final-list :stock-name stock-name})))])
    (-> ""
        ring-resp/response
        (ring-resp/content-type "text/event-stream"))))


(definterceptor session-interceptor
  (middlewares/session {:store (rcookie/cookie-store)} ))

(defroutes routes
  [[["/" {:get home-page}

     ^:interceptors [body-params/body-params, session-interceptor]
     ["/list-filtered-input" {:get list-filtered-input}]
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
