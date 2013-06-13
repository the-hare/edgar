(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]

            [server.handler :as shandler]
            [ui.graph :as graph]
            [ui.components :as components]))


(components/populate-multiselect ".multiselect-live" {:onChange (fn [element checked]

                                                       (if checked
                                                         ($/post (str "/get-streaming-stock-data?stock-selection=" (.val element) "&stock-name=" (.text element))
                                                                 (fn [data]
                                                                   (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))})
(.click ($ "#freeform-live") (fn [eventObj]

                               (let [input-val (.val ($ "#freeform-live-input"))]

                                 (.log js/console "... here[" eventObj "] / input[" input-val "]")
                                 (if (not (empty? input-val))

                                   ($/post (str "/get-streaming-stock-data?stock-selection=" input-val "&stock-name=" input-val)
                                           (fn [data]
                                             (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))))


(components/populate-multiselect ".multiselect-historical" {:onChange (fn [element checked]

                                                             (if checked
                                                               ($/ajax "/get-historical-data"
                                                                       (clj->js {:data {:stock-selection (.val element)
                                                                                        :time-duration "300 S"
                                                                                        :time-interval "1 secs"}
                                                                                 :complete (fn [jqXHR status]

                                                                                             (.log js/console (str ".multiselect-historical > jqXHR[" jqXHR "] / status[" status "]"))
                                                                                             (let [result-data (reader/read-string (.-responseText jqXHR))
                                                                                                   parsed-result-map (shandler/parse-result-data result-data)
                                                                                                   increment? false]

                                                                                               (.log js/console (str "... generated signal-map[" (-> result-data :strategies) "]"))
                                                                                               (graph/render-stock-graph "#historical-stock-graph"
                                                                                                                   [(:bollinger-band parsed-result-map)
                                                                                                                    (:local-list parsed-result-map)
                                                                                                                    (:sma-list parsed-result-map)
                                                                                                                    (:ema-list parsed-result-map)


                                                                                                                    (:macd-price-list parsed-result-map)
                                                                                                                    (:macd-signal-list parsed-result-map)
                                                                                                                    (:macd-histogram-list parsed-result-map)

                                                                                                                    (:stochastic-k parsed-result-map)
                                                                                                                    (:stochastic-d parsed-result-map)

                                                                                                                    (:obv parsed-result-map)]
                                                                                                                   (:signals parsed-result-map)
                                                                                                                   (:strategies parsed-result-map)
                                                                                                                   (:stock-name parsed-result-map)
                                                                                                                   increment?)))}))))})

(def livesource (js/window.EventSource. "/get-streaming-stock-data"))
(.addEventListener livesource
                   "stream-live"
                   (fn [e]

                     (let [result-data (reader/read-string (.-data e))
                           parsed-result-map (shandler/parse-result-data result-data)
                           increment?  (and (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart"))))
                                            (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title))))
                                            (= (:stock-name parsed-result-map)
                                               (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title) (.-text)))) ]

                       (graph/render-stock-graph "#live-stock-graph"
                                           [(:bollinger-band parsed-result-map)
                                            (:local-list parsed-result-map)
                                            (:sma-list parsed-result-map)
                                            (:ema-list parsed-result-map)


                                            (:macd-price-list parsed-result-map)
                                            (:macd-signal-list parsed-result-map)
                                            (:macd-histogram-list parsed-result-map)

                                            (:stochastic-k parsed-result-map)
                                            (:stochastic-d parsed-result-map)

                                            (:obv parsed-result-map)]
                                           (:signals parsed-result-map)
                                           (:strategies parsed-result-map)
                                           (:stock-name parsed-result-map)
                                           increment?))))
