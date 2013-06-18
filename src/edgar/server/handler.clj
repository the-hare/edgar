(ns edgar.server.handler)

(defn parse-result-data [result-data]

  {:local-list (into-array (reduce (fn [rslt ech]
                                     (conj rslt [(js/window.parseInt (first ech))
                                                 (js/window.parseFloat (second ech))]))
                                   []
                                   (:stock-list result-data)))


   ;; Basic Long and Short Moving Averages
   :sma-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt [(js/window.parseInt (first ech))
                                               (js/window.parseFloat (second ech))]))
                                 []
                                 (remove #(nil? (first %))
                                         (:sma-list result-data))))

   :ema-list (into-array (reduce (fn [rslt ech]
                                   (conj rslt [(js/window.parseInt (first ech))
                                               (js/window.parseFloat (second ech))]))
                                 []
                                 (remove #(nil? (first %))
                                         (:ema-list result-data))))


   ;; Bollinger-Band Data
   :bollinger-band (into-array (reduce (fn [rslt ech]
                                         (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                     (js/window.parseFloat (:lower-band ech))
                                                     (js/window.parseFloat (:upper-band ech))]))
                                       []
                                       (remove nil? (-> result-data :signals :bollinger-band))))

   ;; MACD Data
   :macd-price-list (into-array (reduce (fn [rslt ech]
                                          (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                      (js/window.parseFloat (:last-trade-macd ech))]))
                                       []
                                       (remove nil? (-> result-data :signals :macd))))

   :macd-signal-list (into-array (reduce (fn [rslt ech]
                                           (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                       (js/window.parseFloat (:ema-signal ech))]))
                                         []
                                         (remove nil? (-> result-data :signals :macd))))

   :macd-histogram-list (into-array (reduce (fn [rslt ech]
                                              (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                          (js/window.parseFloat (:histogram ech))]))
                                            []
                                            (remove nil? (-> result-data :signals :macd))))

   ;; Stochastic Oscillator
   :stochastic-k (into-array (reduce (fn [rslt ech]
                                       (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                   (js/window.parseFloat (:K ech))]))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :stochastic-d (into-array (reduce (fn [rslt ech]
                                       (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                                   (js/window.parseFloat (:D ech))]))
                                     []
                                     (remove nil? (-> result-data :signals :stochastic-oscillator))))

   :obv (into-array (reduce (fn [rslt ech]
                              (conj rslt [(js/window.parseInt (:last-trade-time ech))
                                          (js/window.parseInt (:obv ech))]))
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
