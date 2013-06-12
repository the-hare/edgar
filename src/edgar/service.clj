(ns edgar.service
  (:import [javax.servlet.http HttpServletRequest HttpServletResponse]
           [java.text SimpleDateFormat])
  (:require [edgar.core.edgar :as edgar]
            [edgar.ib.market :as market]
            [edgar.datomic :as edatomic]
            [edgar.ib.handler.live :as live]
            [edgar.core.analysis.lagging :as alagging]
            [edgar.core.signal.lagging :as slagging]
            [edgar.core.signal.leading :as sleading]
            [edgar.core.signal.confirming :as sconfirming]
            [edgar.core.strategy.strategy :as strategy]

            [clojure.java.io :as io]
            [clojure.walk :as walk]
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
            [ring.util.response :as ring-resp]))



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

                                                                                 (let [

                                                                                       ;; from Historical feed, dates will be strings that look like: "20130606  15:33:00"
                                                                                       date-format (SimpleDateFormat. "yyyyMMdd HH:mm:ss")

                                                                                       tick-list-formatted (map (fn [inp]
                                                                                                                  {:last-trade-price (:close inp)
                                                                                                                   :last-trade-time (->> (:date inp)
                                                                                                                                         (.parse date-format)
                                                                                                                                         .getTime)
                                                                                                                   :total-volume (:volume inp)})
                                                                                                                (reverse (walk/keywordize-keys
                                                                                                                          (-> tick-list first :event-list))))


                                                                                       final-list (reduce (fn [rslt ech]
                                                                                                            (conj rslt [(:last-trade-time ech) (:last-trade-price ech)]))
                                                                                                          []
                                                                                                          tick-list-formatted)


                                                                                       sma-list (alagging/simple-moving-average {:input :last-trade-price
                                                                                                                                 :output :last-trade-price-average
                                                                                                                                 :etal [:last-trade-price :last-trade-time]}
                                                                                                                                20
                                                                                                                                tick-list-formatted)
                                                                                       smaF (reduce (fn [rslt ech]
                                                                                                      (conj rslt [(:last-trade-time ech) (:last-trade-price-average ech)]))
                                                                                                    []
                                                                                                    sma-list)

                                                                                       ema-list (alagging/exponential-moving-average nil 20 tick-list-formatted sma-list)
                                                                                       emaF (reduce (fn [rslt ech]
                                                                                                      (conj rslt [(:last-trade-time ech) (:last-trade-price-exponential ech)]))
                                                                                                    []
                                                                                                    ema-list)

                                                                                       signals-ma (slagging/moving-averages 20 tick-list-formatted sma-list ema-list)
                                                                                       signals-bollinger (slagging/bollinger-band 20 tick-list-formatted sma-list)
                                                                                       signals-macd (sleading/macd nil 20 tick-list-formatted sma-list)
                                                                                       signals-stochastic (sleading/stochastic-oscillator 14 3 3 tick-list-formatted)
                                                                                       signals-obv (sconfirming/on-balance-volume 10 tick-list-formatted)

                                                                                       sA (strategy/strategy-fill-A tick-list-formatted
                                                                                                                    signals-ma
                                                                                                                    signals-bollinger
                                                                                                                    signals-macd
                                                                                                                    signals-stochastic
                                                                                                                    signals-obv)
                                                                                       sB (strategy/strategy-fill-B tick-list-formatted
                                                                                                                    signals-ma
                                                                                                                    signals-bollinger
                                                                                                                    signals-macd
                                                                                                                    signals-stochastic
                                                                                                                    signals-obv)]


                                                                                   ((:resume-fn paused-context) {:stock-name (-> tick-list first :company)
                                                                                                                 :stock-list final-list
                                                                                                                 :source-list tick-list
                                                                                                                 :sma-list smaF
                                                                                                                 :ema-list emaF
                                                                                                                 :signals {:moving-average signals-ma
                                                                                                                           :bollinger-band signals-bollinger
                                                                                                                           :macd signals-macd
                                                                                                                           :stochastic-oscillator signals-stochastic
                                                                                                                           :obv signals-obv}
                                                                                                                 :strategies {:strategy-A sA
                                                                                                                              :strategy-B sB}})))])))
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

  (let [client (:interactive-brokers-client edgar/*interactive-brokers-workbench*)
        stock-selection [ (-> request :query-params :stock-selection) ]
        stock-name (-> request :query-params :stock-name)]

    (edgar/play-live client stock-selection [(fn [tick-list tick]

                                               (let [tick-list-N (map (fn [inp]
                                                                        (assoc inp
                                                                          :total-volume (read-string (:total-volume inp))
                                                                          :last-trade-size (read-string (:last-trade-size inp))
                                                                          :vwap (read-string (:vwap inp))
                                                                          :last-trade-price (read-string (:last-trade-price inp))))
                                                                      (reverse tick-list))

                                                     final-list (reduce (fn [rslt ech]
                                                                          (conj rslt [(:last-trade-time ech) (:last-trade-price ech)]))
                                                                        []
                                                                        tick-list-N)


                                                     sma-list (alagging/simple-moving-average nil 20 tick-list-N)
                                                     smaF (reduce (fn [rslt ech]
                                                                    (conj rslt [(:last-trade-time ech) (:last-trade-price-average ech)]))
                                                                  []
                                                                  sma-list)

                                                     ema-list (alagging/exponential-moving-average nil 20 tick-list-N sma-list)
                                                     emaF (reduce (fn [rslt ech]
                                                                    (conj rslt [(:last-trade-time ech) (:last-trade-price-exponential ech)]))
                                                                  []
                                                                  ema-list)

                                                     signals-ma (slagging/moving-averages 20 tick-list-N sma-list ema-list)
                                                     signals-bollinger (slagging/bollinger-band 20 tick-list-N sma-list)
                                                     signals-macd (sleading/macd nil 20 tick-list-N sma-list)
                                                     signals-stochastic (sleading/stochastic-oscillator 14 3 3 tick-list-N)
                                                     signals-obv (sconfirming/on-balance-volume 10 tick-list-N)

                                                     #_sA #_(strategy/strategy-A tick-list-N
                                                                             signals-ma
                                                                             signals-bollinger
                                                                             signals-macd
                                                                             signals-stochastic
                                                                             signals-obv)

                                                     sA [(assoc (first tick-list-N) :strategies [{:signal :up
                                                                                                  :why "test"}])]
                                                     sB (strategy/strategy-B tick-list-N
                                                                             signals-ma
                                                                             signals-bollinger
                                                                             signals-macd
                                                                             signals-stochastic
                                                                             signals-obv)]

                                                 #_(println (str "... strategy-A[" sA "]"))
                                                 #_(println (str "... strategy-B[" sB "]"))

                                                 (stream-live "stream-live" {:stock-name stock-name
                                                                             :stock-list final-list
                                                                             :source-list tick-list-N
                                                                             :sma-list smaF
                                                                             :ema-list emaF
                                                                             :signals {:moving-average signals-ma
                                                                                       :bollinger-band signals-bollinger
                                                                                       :macd signals-macd
                                                                                       :stochastic-oscillator signals-stochastic
                                                                                       :obv signals-obv}
                                                                             :strategies {:strategy-A sA
                                                                                          :strategy-B sB}})))])
    { :status 204 }))


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
