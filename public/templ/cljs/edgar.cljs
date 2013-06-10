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
                                   {:names [label "Simple Moving Average" "Exponential Moving Average" "Bolling Band"]
                                    :rangeSelector {:selected 7}
                                    :title {:text label}
                                    :chart {:zoomType "x"}
                                    :navigator {:adaptToUpdatedData true}
                                    :yAxis [{
                                             :title {:text "Technical Analysis"}
                                             :height 200}
                                            {
                                             :title {:text "MACD / Signal"}
                                             :height 100
                                             :top 300
                                             :offset 0
                                             :lineWidth 2}
                                            {
                                             :title {:text "MACD Histog"}
                                             :height 100
                                             :top 400
                                             :offset 0
                                             :lineWidth 2}
                                            {
                                             :title {:text "Stochastic Osc"}
                                             :height 100
                                             :top 500
                                             :offset 0
                                             :lineWidth 2
                                             :max 1
                                             :min 0
                                             :plotLines [{
                                                          :value 0.75
                                                          :color "red"
                                                          :width 1
                                                          :dashStyle "longdash"
                                                          :label {:text "Overbought"}}
                                                         {
                                                          :value 0.25
                                                          :color "green"
                                                          :width 1
                                                          :dashStyle "longdash"
                                                          :label {:text "Oversold"}}]}
                                            {
                                             :title {:text "OBV"}
                                             :height 100
                                             :top 600
                                             :offset 0
                                             :lineWidth 2}]

                                    :series [{:name label,
                                              :data (reverse (first dataList))
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Simple Moving Average"
                                              :data (reverse (second dataList))
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Exponential Moving Average"
                                              :data (reverse (nth dataList 2))
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Bollinger Band"
                                              :data (reverse (nth dataList 3))
                                              :type "arearange"
                                              :color "#629DFF"
                                              :marker {:enabled true :radius 3}
                                              :tooltip {:valueDecimals 2}}

                                             ;; MACD Data
                                             {:name "MACD Price"
                                              :data (reverse (nth dataList 4))
                                              :yAxis 1
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}
                                             {:name "MACD Signal"
                                              :data (reverse (nth dataList 5))
                                              :yAxis 1
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}

                                             {:name "MACD Histogram"
                                              :data (reverse (nth dataList 6))
                                              :yAxis 2
                                              :type "column"
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}

                                             ;; Stochastic Data
                                             {:name "Stochastic K"
                                              :data (reverse (nth dataList 7))
                                              :yAxis 3
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}
                                             {:name "Stochastic D"
                                              :data (reverse (nth dataList 8))
                                              :yAxis 3
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}

                                             {:name "On Balance Volume"
                                              :data (reverse (nth dataList 9))
                                              :yAxis 4
                                              :type "column"
                                              :marker {:enabled true :radius 3}
                                              :shadow true
                                              :tooltip {:valueDecimals 2}}]})))
    (do

      (-> ($ selector)
          (.highcharts)
          (.-series)
          first
          (.addPoint (last (reverse (first dataList))) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          second
          (.addPoint (last (reverse (second dataList))) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 2)
          (.addPoint (last (reverse (nth dataList 2))) true false))

      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 3)
          (.addPoint (last (reverse (nth dataList 3))) true false)))))



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



(defn parse-result-data [result-data]

  {:local-list (into-array (reduce (fn [rslt ech]
                                     (conj rslt (into-array [(js/window.parseInt (first ech))
                                                             (js/window.parseFloat (second ech))])))
                                   []
                                   (into-array (:stock-list result-data))))


   ;; Basic Long and Short Moving Averages
   :sma-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt (into-array [(js/window.parseInt (first ech))
                                                           (js/window.parseFloat (second ech))])))
                                 []
                                 (remove #(nil? (first %))
                                         (into-array (:sma-list result-data)))))

   :ema-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt (into-array [(js/window.parseInt (first ech))
                                                           (js/window.parseFloat (second ech))])))
                                 []
                                 (remove #(nil? (first %))
                                         (into-array (:ema-list result-data)))))


   ;; Bollinger-Band Data
   :bollinger-band (into-array (reduce (fn [rslt ech]
                                         (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                 (js/window.parseFloat (:lower-band ech))
                                                                 (js/window.parseFloat (:upper-band ech))])))
                                       []
                                       (remove nil? (-> result-data :signals :bollinger-band))))

   ;; MACD Data
   :macd-price-list (into-array (reduce (fn [rslt ech]
                                         (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                 (js/window.parseFloat (:last-trade-macd ech))])))
                                       []
                                       (remove nil? (-> result-data :signals :macd))))

   :macd-signal-list (into-array (reduce (fn [rslt ech]
                                           (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                   (js/window.parseFloat (:ema-signal ech))])))
                                         []
                                         (remove nil? (-> result-data :signals :macd))))

   :macd-histogram-list (into-array (reduce (fn [rslt ech]
                                              (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                                      (js/window.parseFloat (:histogram ech))])))
                                            []
                                            (remove nil? (-> result-data :signals :macd))))

   ;; Stochastic Oscillator
   :stochastic-k (into-array (reduce (fn [rslt ech]
                                       (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                               (js/window.parseFloat (:K ech))])))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :stochastic-d (into-array (reduce (fn [rslt ech]
                                       (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                               (js/window.parseFloat (:D ech))])))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :obv (into-array (reduce (fn [rslt ech]
                              (conj rslt (into-array [(js/window.parseInt (:last-trade-time ech))
                                                      (js/window.parseInt (:obv ech))])))
                            []
                            (remove nil? (-> result-data :signals :obv))))

   :stock-name (:stock-name result-data)})


(populate-multiselect ".multiselect-live" {:onChange (fn [element checked]

                                                       (if checked
                                                         ($/post (str "/get-streaming-stock-data?stock-selection=" (.val element) "&stock-name=" (.text element))
                                                                 (fn [data]
                                                                   (.log js/console (str "POST:: get-streaming-stock-data > data[" data "]"))))))})
(.click ($ "#freeform-live") (fn [eventObj]
                               (.log js/console "... here[" eventObj "] / input[" (.val ($ "#freeform-live-input")) "]")))


(populate-multiselect ".multiselect-historical" {:onChange (fn [element checked]

                                                             (if checked
                                                               ($/ajax "/get-historical-data"
                                                                       (clj->js {:data {:stock-selection (.val element)
                                                                                        :time-duration "300 S"
                                                                                        :time-interval "1 secs"}
                                                                                 :complete (fn [jqXHR status]

                                                                                             (.log js/console (str ".multiselect-historical > jqXHR[" jqXHR "] / status[" status "]"))
                                                                                             (let [result-data (reader/read-string (.-responseText jqXHR))
                                                                                                   parsed-result-map (parse-result-data result-data)
                                                                                                   increment? false]

                                                                                               (render-stock-graph "#historical-stock-graph"
                                                                                                                   [(:local-list parsed-result-map)
                                                                                                                    (:sma-list parsed-result-map)
                                                                                                                    (:ema-list parsed-result-map)
                                                                                                                    (:bollinger-band parsed-result-map)

                                                                                                                    (:macd-price-list parsed-result-map)
                                                                                                                    (:macd-signal-list parsed-result-map)
                                                                                                                    (:macd-histogram-list parsed-result-map)

                                                                                                                    (:stochastic-k parsed-result-map)
                                                                                                                    (:stochastic-d parsed-result-map)

                                                                                                                    (:obv parsed-result-map)]
                                                                                                                   (:stock-name parsed-result-map)
                                                                                                                   increment?)))}))))})

(def livesource (js/window.EventSource. "/get-streaming-stock-data"))
(.addEventListener livesource
                   "stream-live"
                   (fn [e]

                     (let [result-data (reader/read-string (.-data e))
                           parsed-result-map (parse-result-data result-data)
                           increment?  (and (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart"))))
                                            (= (:stock-name parsed-result-map)
                                               (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title) (.-text)))) ]

                       (render-stock-graph "#live-stock-graph"
                                           [(:local-list parsed-result-map)
                                            (:sma-list parsed-result-map)
                                            (:ema-list parsed-result-map)
                                            (:bollinger-band parsed-result-map)]
                                           (:stock-name parsed-result-map)
                                           increment?))))
