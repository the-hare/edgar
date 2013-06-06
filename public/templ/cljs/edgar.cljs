(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]))


;; === SCROLLING  with Lionbars
#_(.lionbars ($ ".body-container"))



;; === RENDER the Live stock graph
(defn render-stock-graph [selector dataList label increment]

  (if-not increment

    (-> ($ selector)
        (.highcharts "StockChart" (clj->js
                                   {:names [label "Simple Moving Average" "Exponential Moving Average"]
                                    :rangeSelector {:selected 3}
                                    :title {:text label}
                                    :chart {:zoomType "x"}
                                    :navigator {:adaptToUpdatedData true}
                                    :series [{:name label,
                                              :data (reverse (first dataList))
                                              :marker {:enabled true, :radius 3}
                                              :shadow true,
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Simple Moving Average"
                                              :data (reverse (second dataList))
                                              :marker {:enabled true, :radius 3}
                                              :shadow true,
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Exponential Moving Average"
                                              :data (reverse (nth dataList 2))
                                              :marker {:enabled true, :radius 3}
                                              :shadow true,
                                              :tooltip {:valueDecimals 2}}]})
                     ))
    (do

      (-> ($ selector)
          (.highcharts)
          (.-series)
          first
          (.addPoint (last (first dataList)) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          second
          (.addPoint (last (second dataList)) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 2)
          (.addPoint (last (nth dataList 2)) true false)))))

(def tick-list (clj->js [[1368215573010 203.98] [1368215576331 203.99] [1368215576857 203.99] [1368215577765 203.99] [1368215578769 204.0] [1368215579272 204.01] [1368215579517 204.02] [1368215581769 204.02] [1368215583602 204.01] [1368215585650 204.02] [1368215586060 204.02] [1368215587029 204.01] [1368215588318 204.02] [1368215589335 204.01] [1368215589536 204.01] [1368215589846 204.0] [1368215591079 203.99] [1368215591789 203.99] [1368215592104 203.98] [1368215592615 203.98] [1368215592758 203.99] [1368215594039 203.97] [1368215597119 203.98] [1368215597632 203.97] [1368215599396 203.97] [1368215603876 203.96] [1368215606059 203.96] [1368215610316 203.95] [1368215610634 203.95] [1368215610813 203.93] [1368215612886 203.95] [1368215615858 203.94] [1368215618621 203.94] [1368215619138 203.96] [1368215623846 203.94] [1368215632669 203.94] [1368215634709 203.92] [1368215636587 203.93] [1368215636952 203.94] [1368215638328 203.93]]))

#_(render-stock-graph "#live-stock-graph" tick-list "IBM" false)
#_(render-stock-graph "#historical-stock-graph" tick-list "AAPL" false)



;; === POPULATE the live multi-select
(defn populate-multiselect [selector options]

  (let-deferred [filtered-input ($/ajax "/list-filtered-input")]

                (let [multiselect ($ selector)]

                  (reduce (fn [rslt inp]

                            (let [option-value (second inp)
                                  option-label (nth inp 2)
                                  price-difference (.toFixed (first inp) 2)]

                              (-> multiselect
                                  (.append (str "<option value='" option-value "'>" option-label " (" price-difference ")</option>")))))
                          nil
                          (into-array (reader/read-string filtered-input)))

                  (-> ($ selector)
                      (.multiselect (clj->js (merge {:enableFiltering true} options)))))))


(populate-multiselect ".multiselect-live" {:onChange (fn [element checked]

                                                       (if checked
                                                         ($/post (str "/get-streaming-stock-data?stock-selection=" (.val element) "&stock-name=" (.text element))
                                                                 (fn [data]
                                                                   (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))})


(populate-multiselect ".multiselect-historical" {:onChange (fn [element checked]

                                                             (if checked
                                                               ($/ajax "/get-historical-data"
                                                                       (clj->js {:data {:stock-selection (.val element)
                                                                                        :time-duration "60 S"
                                                                                        :time-interval "1 secs"}
                                                                                 :complete (fn [jqXHR status]

                                                                                             (.log js/console (str ".multiselect-historical > jqXHR[" jqXHR "] / status[" status "]"))
                                                                                             (let [result-data (reader/read-string (.-responseText jqXHR))
                                                                                                   local-list (:stock-list result-data)
                                                                                                   stock-name (:stock-name result-data)]

                                                                                               (render-stock-graph "#historical-stock-graph" local-list stock-name false)))}))))})

(def livesource (js/window.EventSource. "/get-streaming-stock-data"))
(.addEventListener livesource
                   "stream-live"
                   (fn [e]

                     #_(.log js/console (str "GET:: get-streaming-live-data > e[" e "]"))
                     (let [result-data (reader/read-string (.-data e))

                           local-list (into-array (reduce (fn [rslt ech]
                                                            (conj rslt (into-array [(js/window.parseInt (first ech)) (js/window.parseFloat (second ech))])))
                                                          []
                                                          (into-array (:stock-list result-data))))

                           sma-list (into-array (reduce (fn [rslt ech]
                                                            (conj rslt (into-array [(js/window.parseInt (first ech)) (js/window.parseFloat (second ech))])))
                                                        []
                                                        (remove #(nil? (first %))
                                                                (into-array (:sma-list result-data)))))

                           ema-list (into-array (reduce (fn [rslt ech]
                                                            (conj rslt (into-array [(js/window.parseInt (first ech)) (js/window.parseFloat (second ech))])))
                                                        []
                                                        (remove #(nil? (first %))
                                                                (into-array (:ema-list result-data)))))


                           stock-name (:stock-name result-data)
                           increment?  (and (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart"))))
                                            (= stock-name
                                               (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title) (.-text)))) ]

                       (.log js/console "")
                       (.log js/console (str "...local-list[" local-list "]"))
                       (.log js/console (str "...sma-list[" sma-list "]"))
                       (render-stock-graph "#live-stock-graph" [local-list sma-list ema-list] stock-name increment?))))
