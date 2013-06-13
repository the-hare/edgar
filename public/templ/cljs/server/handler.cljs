(ns server.handler)


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
