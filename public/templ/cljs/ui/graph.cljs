(ns ui.graph
  (:use [jayq.core :only [$ css inner]]))



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


  #_(.log js/console "... add-strategies > strategy-map[" strategy-map "]")
  (let [default-entry {:type "flags"
                       :name "strategies"
                       :data []
                       :onSeries "tick-list"
                       :color "#5F86B3"
                       :fillColor "#5F86B3"
                       :width 16
                       :style {:color "white"}
                       :states {:hover { :fillColor "#395C84" }}}

        data-list (->> (reduce (fn [rslt ech-list]

                                 (concat rslt (reduce (fn [rF eF] (conj rF eF))
                                                      []
                                                      (second ech-list))))
                               []
                               (seq strategy-map))

                       (remove #(or (nil? %) (empty? %))))]


    #_(.log js/console "... RESULT data-list[" (clj->js data-list) "]")
    (conj initial-list (assoc default-entry :data data-list))))



(defn build-graph-series-data [dataList signal-map strategy-map]

  #_(.log js/console (str "... build-graph-series-data > 1[" (into-array (reverse (map (fn [inp] [(js/window.parseInt (first inp)) (second inp) (nth inp 2)])
                                                                                     (first dataList)))) "]"))
  (let [initial-list [{:name "Bollinger Band"
                       :id "bollinger-list"
                       :data (reverse (first dataList))
                       :type "arearange"
                       :color "#629DFF"
                       :marker {:enabled true :radius 3}
                       :tooltip {:valueDecimals 2}}
                      {:name "Closing Price"
                       :id "tick-list"
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
                       :tooltip {:valueDecimals 2}}]

        #_with-signals #_(add-signals initial-list signal-map)  ;; iterate over map entries
        with-strategies (add-strategies initial-list strategy-map)]

    #_(.log js/console (str "... FINAL series array[" with-strategies "]"))
    with-strategies))


(defn chart-fill [selector dataList signal-map strategy-map label]

  (-> ($ selector)
      (.highcharts "StockChart" (clj->js
                                 {:names [label "Bolling Band" "Simple Moving Average" "Exponential Moving Average"]
                                  :rangeSelector {:selected 11}
                                  :title {:text label}
                                  :chart {:zoomType "x"}
                                  :navigator {:adaptToUpdatedData true}
                                  :yAxis [{
                                           :title {:text "Technical Analysis"}
                                           :height 200
                                           :shadow false
                                           :turboThreshold 50
                                           :marker {:enabled false}}
                                          {
                                           :title {:text "MACD / Signal"}
                                           :height 100
                                           :top 300
                                           :offset 0
                                           :lineWidth 2
                                           :turboThreshold 50
                                           :shadow false
                                           :marker {:enabled false}
                                           :plotOptions{:series {:enableMouseTracking false}}}
                                          {
                                           :title {:text "MACD Histog"}
                                           :height 100
                                           :top 400
                                           :offset 0
                                           :lineWidth 2
                                           :turboThreshold 50
                                           :shadow false
                                           :marker {:enabled false}
                                           :plotOptions{:series {:enableMouseTracking false}}}
                                          {
                                           :title {:text "Stochastic Osc"}
                                           :height 100
                                           :top 500
                                           :offset 0
                                           :lineWidth 2
                                           :max 1
                                           :min 0
                                           :turboThreshold 50
                                           :shadow false
                                           :marker {:enabled false}
                                           :plotOptions{:series {:enableMouseTracking false}}
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
                                           :lineWidth 2
                                           :turboThreshold 50
                                           :shadow false
                                           :marker {:enabled false}
                                           :plotOptions{:series {:enableMouseTracking false}}}]

                                  :series (build-graph-series-data dataList signal-map strategy-map)}))))


(defn chart-increment [selector dataList strategy-map label]

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
          (.addPoint (last (reverse (nth dataList 3))) true false))


      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 4)
          (.addPoint (last (reverse (nth dataList 4))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 5)
          (.addPoint (last (reverse (nth dataList 5))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 6)
          (.addPoint (last (reverse (nth dataList 6))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 7)
          (.addPoint (last (reverse (nth dataList 7))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 8)
          (.addPoint (last (reverse (nth dataList 8))) true false))
      (-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 9)
          (.addPoint (last (reverse (nth dataList 9))) true false))


      #_(.log js/console "")
      #_(.log js/console (str "Zzz 1[" (last (reverse (second dataList))) "]"))
      #_(.log js/console (str "Zzz 2[" {:x (js/window.Date. (nth (last (reverse (second dataList))) 10)) :title "Testing 123"} "]"))


      #_(-> ($ selector)
          (.highcharts)
          (.-series)
          (nth 10)
          (.addPoint {:x (js/window.Date. (nth (last (reverse (second dataList))) 10)) :title "Testing 123"} true false))))

(defn render-stock-graph [selector dataList signal-map strategy-map label increment]


  #_(.log js/console (str "... render-stock-graph > strategy-map[" strategy-map "] > increment[" increment "]"))
  (if-not increment

    (chart-fill selector dataList signal-map strategy-map label)
    (chart-increment selector dataList strategy-map label)))
