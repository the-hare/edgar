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
            [edgar.core.strategy.target :as target]
            [edgar.core.signal.common :as common]
            [edgar.server.handler :as shandler]

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

    ;; Output here will be a map with a key / value of { :stock-name tick-list }
    (edgar/play-historical client stock-selection time-duration time-interval [(fn [tick-list]

                                                                                 (market/close-market-channel)

                                                                                 ;; tick-list format will be:
                                                                                 ;; [{:id 0, :symbol DDD, :company 3D Systems Corporation, :price-difference 0.09000000000000341, :event-list []}]
                                                                                 (reduce (fn [rslt ech-list]

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
                                                                                                                                    (-> ech-list :event-list))))


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

                                                                                                 #_sA #_[(assoc (nth tick-list-formatted 10) :strategies [{:signal :up
                                                                                                                                                           :why "test"}])]

                                                                                                 sB (strategy/strategy-fill-B tick-list-formatted
                                                                                                                              signals-ma
                                                                                                                              signals-bollinger
                                                                                                                              signals-macd
                                                                                                                              signals-stochastic
                                                                                                                              signals-obv)]


                                                                                             ((:resume-fn paused-context) {:stock-name (:company ech-list)
                                                                                                                           :stock-symbol (:symbol ech-list)
                                                                                                                           :stock-list final-list
                                                                                                                           :source-list ech-list
                                                                                                                           :sma-list smaF
                                                                                                                           :ema-list emaF
                                                                                                                           :signals {:moving-average signals-ma
                                                                                                                                     :bollinger-band signals-bollinger
                                                                                                                                     :macd signals-macd
                                                                                                                                     :stochastic-oscillator signals-stochastic
                                                                                                                                     :obv signals-obv}
                                                                                                                           :strategies {:strategy-A sA
                                                                                                                                        :strategy-B sB}})))
                                                                                         []
                                                                                         tick-list))])))


(defbefore get-historical-data
  "Get historical data for a particular stock"
  [{request :request :as context}]

  (iimpl/with-pause [paused-context context]
    (async-historical
        (assoc paused-context :resume-fn (partial resume-historical paused-context)))))



;; LIVE Data
(def stored-streaming-context (atom nil))
(def tracking-data (ref []))


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


(defn watch-strategies
  "Tracks and instruments existing strategies in play"
  [tracking-data tick-list]

  ;; check if latest tick matches a stock being watched
  (if (some #(= % (:tickerId (first tick-list)))
            (map :tickerId @tracking-data))

    (dosync (alter tracking-data (fn [inp]

                                   (let [result-filter (filter #(= (-> % second :tickerId) (:tickerId (first tick-list)))
                                                               (map-indexed (fn [idx itm] [idx itm]) inp))]

                                     (println (str "... 2 > WATCH > result-filter[" (seq result-filter) "]"))

                                     ;; update-in-place, the existing tracking-data
                                     ;; i. find index of relevent entry
                                     (update-in inp
                                                [(first (map first result-filter))]
                                                (fn [i1]

                                                  (println (str "... 3 > WATCH > update-in inp[" i1 "]"))
                                                  (let [

                                                        ;; find peaks-valleys
                                                        peaks-valleys (common/find-peaks-valleys nil tick-list)
                                                        peaks (:peak (group-by :signal peaks-valleys))

                                                        stoploss-threshold? (target/stoploss-threshhold? (:orig-trade-price i1) (:last-trade-price (first tick-list)))
                                                        reached-target? (target/target-threshhold? (:orig-trade-price i1) (:last-trade-price (first tick-list)))

                                                        ;; ensure we're not below stop-loss
                                                        ;; are we: at 'target'

                                                        ;; OR

                                                        ;; are we: abouve last 2 peaks - hold
                                                        ;; are we: below first peak, but abouve second peak - hold
                                                        ;; are we: below previous 2 peaks - sell

                                                        action (if stoploss-threshold?

                                                                 {:action :down :why :stoploss-threshold}

                                                                 (if (every? #(>= (:last-trade-price (first tick-list))
                                                                                  (:last-trade-price %))
                                                                             (take 2 peaks))

                                                                   {:action :up :why :abouve-last-2-peaks}

                                                                   (if (and (>= (:last-trade-price (first tick-list))
                                                                                (:last-trade-price (nth tick-list 2)))
                                                                            (<= (:last-trade-price (first tick-list))
                                                                                (:last-trade-price (second tick-list))))

                                                                     {:action :up :why :abouve-second-below-first-peak}

                                                                     {:action :down :why :below-first-2-peaks})))


                                                        price-diff (- (:last-trade-price (first tick-list)) (:orig-trade-price i1))
                                                        merge-result (merge i1 {:last-trade-price (:last-trade-price (first tick-list))
                                                                                :last-trade-time (:last-trade-time (first tick-list))
                                                                                :change-pct (/ price-diff (:orig-trade-price i1))
                                                                                :change-prc price-diff
                                                                                :action action})]

                                                    (println (str "... 4 > WATCH > result[" merge-result "]"))
                                                    merge-result)))))))))

(defn track-strategies
  "Follows new strategy recommendations coming in"
  [tick-list strategy-list]

  ;; iterate through list of strategies
  (reduce (fn [rA eA]

            (println (str "... 1 > eA[" eA "] > some test if/else[" (some #(= % (:tickerId eA)) (map :tickerId @tracking-data)) "]"))
            ;; does tickerId of current entry = any tickerIds in existing list?
            (if (some #(= % (:tickerId eA))
                      (map :tickerId @tracking-data))


              ;; for tracking symbols, each new tick -> calculate:
              ;;     $ gain/loss
              ;;     % gain/loss
              (dosync (alter tracking-data (fn [inp]

                                             (let [result-filter (filter #(= (-> % second :tickerId) (:tickerId eA))
                                                                         (map-indexed (fn [idx itm] [idx itm]) inp))]

                                               #_(println (str "... 2 > result-filter[" (seq result-filter) "]"))

                                               ;; update-in-place, the existing tracking-data
                                               ;; i. find index of relevent entry
                                               (update-in inp
                                                          [(first (map first result-filter))]
                                                          (fn [i1]

                                                            #_(println (str "... 3 > update-in inp[" i1 "]"))
                                                            (let [price-diff (- (:last-trade-price (first tick-list)) (:orig-trade-price i1))
                                                                  merge-result (merge i1 {:last-trade-price (:last-trade-price (first tick-list))
                                                                                          :last-trade-time (:last-trade-time eA)
                                                                                          :change-pct (/ price-diff (:orig-trade-price i1))
                                                                                          :change-prc price-diff})]

                                                              #_(println (str "... 4 > result[" merge-result "]"))
                                                              merge-result)))))))

              ;; otherwise store them in a hacked-session
              (dosync (alter tracking-data conj {:uuid (:uuid eA)
                                                 :symbol (:symbol tick-list)
                                                 :tickerId (:tickerId eA)
                                                 :orig-trade-price (:last-trade-price eA)
                                                 :orig-trade-time (:last-trade-time eA)
                                                 :strategies (:strategies eA)
                                                 :source-entry eA}))))
          []
          strategy-list))

(defn get-streaming-stock-data [request]

  (let [client (:interactive-brokers-client edgar/*interactive-brokers-workbench*)
        stock-selection [ (-> request :query-params :stock-selection) ]
        stock-name (-> request :query-params :stock-name)]

    (edgar/play-live client stock-selection [(fn [tick-list]

                                               #_(println (str "get-streaming-stock-data tick-list[" tick-list "]"))
                                               (let [tick-list-N (map (fn [inp]
                                                                        (assoc inp
                                                                          :total-volume (read-string (:total-volume inp))
                                                                          :last-trade-size (read-string (:last-trade-size inp))
                                                                          :vwap (read-string (:vwap inp))
                                                                          :last-trade-price (read-string (:last-trade-price inp))))
                                                                      (reverse (:event-list tick-list)))

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

                                                     sA (strategy/strategy-A tick-list-N
                                                                             signals-ma
                                                                             signals-bollinger
                                                                             signals-macd
                                                                             signals-stochastic
                                                                             signals-obv)

                                                     #_sA #_[(assoc (first tick-list-N) :strategies [{:signal :up
                                                                                                  :name :strategy-test-A
                                                                                                  :why "test-a"}])]
                                                     sB (strategy/strategy-B tick-list-N
                                                                             signals-ma
                                                                             signals-bollinger
                                                                             signals-macd
                                                                             signals-stochastic
                                                                             signals-obv)
                                                     #_sB #_[(assoc (first tick-list-N) :strategies [{:signal :up
                                                                                                  :name :strategy-test-B
                                                                                                  :why "test-b"}])]

                                                     result-data {:stock-name stock-name
                                                                  :stock-symbol (:symbol tick-list)
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
                                                                               :strategy-B sB}}

                                                     #_parsed-result-map #_(shandler/parse-result-data result-data)]

                                                 (println "")
                                                 (println (str "... 0 > tracking-data[" @tracking-data "]"))
                                                 (println (str "... strategy-A[" sA "]"))
                                                 (println (str "... strategy-B[" sB "]"))

                                                 ;; track any STRATEGIES
                                                 (if (or (not (empty? sA))
                                                         (not (empty? sB)))

                                                   (track-strategies tick-list (remove nil? [(first sA) (first sB)])))

                                                 ;; watch any STRATEGIES in play
                                                 (if (not (empty? @tracking-data))

                                                   (watch-strategies tracking-data tick-list))


                                                 (stream-live "stream-live" result-data))
                                               []
                                               tick-list)])
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
