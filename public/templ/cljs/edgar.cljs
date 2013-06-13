(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]))



(defn add-signals [initial-list signal-map]

  (reduce (fn [rslt ech]

            (let [default-entry (fn [eF]
                                  {:type "flags"
                                   :data [{:x (-> eF :x)
                                           :title (-> eF :title)
                                           :text (-> eF :text)}]
                                   :color "#5F86B3"
                                   :fillColor "#5F86B3"
                                   :width 16
                                   :style {:color "white"}
                                   :states {:hover { :fillColor "#395C84" }}})]

              ;; SIGNAL Flags
              (case (first ech)

                :moving-average (concat rslt (reduce (fn [rF eF]        ;; second element is a list of signals
                                                       (conj rF (assoc (default-entry eF) :onSeries "ema-list")))
                                                     []
                                                     (second ech)))

                :bollinger-band (concat rslt (reduce (fn [rF eF]
                                                       (conj rF (assoc (default-entry eF) :onSeries "bollinger-list")))
                                                     []
                                                     (second ech)))

                :macd (concat rslt (reduce (fn [rF eF]
                                             (conj rF (assoc (default-entry eF) :onSeries "macd-price-list")))
                                           []
                                           (second ech)))

                :stochastic-oscillator (concat rslt (reduce (fn [rF eF]
                                                              (conj rF (assoc (default-entry eF) :onSeries "k-list")))
                                                            []
                                                            (second ech)))

                :obv (concat rslt (reduce (fn [rF eF]
                                            (conj rF (assoc (default-entry eF) :onSeries "obv-list")))
                                          []
                                          (second ech)))
                "default" rslt)))
          initial-list
          (seq signal-map)))

(defn add-strategies [initial-list strategy-map]

  (reduce (fn [rslt ech]

            (let [default-entry (fn [eF]
                                  {:type "flags"
                                   :data [{:x (-> eF :x)
                                           :title (-> eF :title)
                                           :text (-> eF :text)}]
                                   :color "#5F86B3"
                                   :fillColor "#5F86B3"
                                   :width 16
                                   :style {:color "white"}
                                   :states {:hover { :fillColor "#395C84" }}})]

              (concat rslt (reduce (fn [rF eF]
                                     (conj rF (assoc (default-entry eF) :onSeries "tick-list")))
                                   []
                                   (second ech)))))
          initial-list
          (seq strategy-map)))



(defn build-graph-series-data [dataList signal-map strategy-map]

  (let [initial-list [{:name "Bollinger Band"
                       :id "bollinger-list"
                       :data (reverse (first dataList))
                       :type "arearange"
                       :color "#629DFF"
                       :marker {:enabled true :radius 3}
                       :tooltip {:valueDecimals 2}}
                      {:name "Closing Price"
                       :id "ticklist"
                       :data (reverse (second dataList))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Simple Moving Average"
                       :id "sma-list"
                       :data (reverse (nth dataList 2))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Exponential Moving Average"
                       :id "ema-list"
                       :data (reverse (nth dataList 3))
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}


                      ;; MACD Data
                      {:name "MACD Price"
                       :id "macd-price-list"
                       :data (reverse (nth dataList 4))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "MACD Signal"
                       :id "macd-signal-list"
                       :data (reverse (nth dataList 5))
                       :yAxis 1
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "MACD Histogram"
                       :id "macd-histogram-list"
                       :data (reverse (nth dataList 6))
                       :yAxis 2
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      ;; Stochastic Data
                      {:name "Stochastic K"
                       :id "k-list"
                       :data (reverse (nth dataList 7))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:name "Stochastic D"
                       :id "d-list"
                       :data (reverse (nth dataList 8))
                       :yAxis 3
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}

                      {:name "On Balance Volume"
                       :id "obv-list"
                       :data (reverse (nth dataList 9))
                       :yAxis 4
                       :type "column"
                       :marker {:enabled true :radius 3}
                       :shadow true
                       :tooltip {:valueDecimals 2}}
                      {:type "flags"
                       :name "strategies"
                       :data []
                       :onSeries "ticklist"
                       :shape "squarepin"
                       }
                      #_{:type "flags"
                       :name "strategies"
                       :shape "squarepin"
                       :data []
                       :onSeries "tick-list"
                       :color "#5F86B3"
                       :fillColor "#5F86B3"
                       :width 16
                       :style {:color "white"}
                       :states {:hover { :fillColor "#395C84" }}}
                      ]

        #_with-signals #_(add-signals initial-list signal-map)  ;; iterate over map entries
        #_with-strategies #_(add-strategies initial-list strategy-map)
        ]

    #_(.log js/console (str "... FINAL series array[" with-strategies "]"))
    initial-list))


;; === RENDER the Live stock graph
(defn render-stock-graph [selector dataList signal-map strategy-map label increment]


  #_(.log js/console (str "... render-stock-graph > strategy-map[" strategy-map "]"))
  (if-not increment

    (-> ($ selector)
        (.highcharts "StockChart" (clj->js
                                   {:names [label "Bolling Band" "Simple Moving Average" "Exponential Moving Average"]
                                    :rangeSelector {:selected 11}
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

                                    :series (build-graph-series-data dataList signal-map strategy-map)})))
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

      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 2)
          (.addPoint (last (reverse (nth dataList 2))) true false))

      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 3)
          (.addPoint (last (reverse (nth dataList 3))) true false))


      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 4)
          (.addPoint (last (reverse (nth dataList 4))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 5)
          (.addPoint (last (reverse (nth dataList 5))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 6)
          (.addPoint (last (reverse (nth dataList 6))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 7)
          (.addPoint (last (reverse (nth dataList 7))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 8)
          (.addPoint (last (reverse (nth dataList 8))) true false))
      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 9)
          (.addPoint (last (reverse (nth dataList 9))) true false))


      (.log js/console "")
      (.log js/console (str "Zzz 1[" (last (reverse (second dataList))) "]"))
      (.log js/console (str "Zzz 2[" {:x (js/window.Date. (first (last (reverse (second dataList))))) :title "Testing 123"} "]"))


      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 10)
          (.addPoint {:x (js/window.Date. (first (last (reverse (second dataList))))) :title "Testing 123"} true false))

      #_(reduce (fn [rslt ech]

                (let [default-entry (fn [eF]
                                      {:x (-> eF :x)
                                       :title (-> eF :title)
                                       :text (-> eF :text)})]

                  (concat rslt (reduce (fn [rF eF]

                                         #_(.log js/console (str "... AND > eF[" eF "]"))
                                         (let [strategy-entry (default-entry eF)]

                                           #_(.log js/console (str "... AND AND > strategy-entry[" strategy-entry "] > fn["  "]"))
                                           (-> ($ selector)
                                               (.highcharts)
                                               (.-series)
                                               (nth 10)
                                               (.addPoint strategy-entry true false))
                                           (conj rF strategy-entry)))
                                       []
                                       (second ech)))))
              []
              (seq strategy-map)))))



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


(defn- pull-out-signals [result-data tag]

  (->> (reduce (fn [rslt ech]

                 (conj rslt (map (fn [inp]   ;; iterate over the :signals list, for each tick entry
                                   {:x (js/window.parseInt (:last-trade-time ech))
                                    :title (:signal inp)
                                    :text (str "Why: " (:why inp))
                                    })
                                 (:signals ech))))
               []
               (remove nil? (-> result-data :signals tag)))
       into-array
       (remove empty?)

       ;; for some strange reason, each list entry is in another list
       (map #(first %))))

(defn- pull-out-strategies [result-data tag]

  (let [result-strategies (->> (reduce (fn [rslt ech]

                                         #_(.log js/console (str "... pulling out strategies[" ech "]"))
                                         (conj rslt (map (fn [inp]
                                                           {:x (js/window.parseInt (:last-trade-time ech))
                                                            :title (:signal inp)
                                                            :text (str "Why: " (:why inp))
                                                            })
                                                         (:strategies ech))))
                                       []
                                       (remove nil? (-> result-data :strategies tag)))
                               into-array
                               (remove empty?)

                               ;; for some strange reason, each list entry is in another list
                               (map #(first %)))]

    #_(.log js/console (str "... pulling out strategies > END[" result-strategies "]"))
    result-strategies))


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

   :signals {:moving-average (pull-out-signals result-data :moving-average)
             :bollinger-band (pull-out-signals result-data :bollinger-band)
             :macd (pull-out-signals result-data :macd)
             :stochastic-oscillator (pull-out-signals result-data :stochastic-oscillator)
             :obv (pull-out-signals result-data :obv)}

   :strategies {:strategy-A (pull-out-strategies result-data :strategy-A)
                :strategy-B (pull-out-strategies result-data :strategy-B)}

   :stock-name (:stock-name result-data)})


(populate-multiselect ".multiselect-live" {:onChange (fn [element checked]

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

                                                                                               (.log js/console (str "... generated signal-map[" (-> result-data :strategies) "]"))
                                                                                               (render-stock-graph "#historical-stock-graph"
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
                           parsed-result-map (parse-result-data result-data)
                           increment?  (and (not (nil? (-> ($ "#live-stock-graph") (.highcharts "StockChart"))))
                                            (= (:stock-name parsed-result-map)
                                               (-> ($ "#live-stock-graph") (.highcharts "StockChart") (.-title) (.-text)))) ]

                       (render-stock-graph "#live-stock-graph"
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
