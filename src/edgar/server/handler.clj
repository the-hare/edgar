(ns edgar.server.handler)


(defn- pull-out-signals [result-data tag]

  (->> (reduce (fn [rslt ech]

                 (conj rslt (map (fn [inp]   ;; iterate over the :signals list, for each tick entry
                                   {:x (:last-trade-time ech)
                                    :title (:signal inp)
                                    :text (str "Why: " (:why inp))
                                    })
                                 (:signals ech))))
               []
               (remove nil? (-> result-data :signals tag)))
       (remove empty?)

       ;; for some strange reason, each list entry is in another list
       (map #(first %))))

(defn- pull-out-strategies [result-data tag]

  (let [result-strategies (->> (reduce (fn [rslt ech]

                                         #_(.log js/console (str "... pulling out strategies[" ech "]"))
                                         (conj rslt (map (fn [inp]
                                                           {:x (:last-trade-time ech)
                                                            :title (:signal inp)
                                                            :text (str "Why: " (:why inp))
                                                            })
                                                         (:strategies ech))))
                                       []
                                       (remove nil? (-> result-data :strategies tag)))
                               (remove empty?)

                               ;; for some strange reason, each list entry is in another list
                               (map #(first %)))]

    #_(.log js/console (str "... pulling out strategies > END[" result-strategies "]"))
    result-strategies))

(defn parse-result-data [result-data]

  {:local-list (reduce (fn [rslt ech]
                         (conj rslt [(first ech)
                                     (second ech)]))
                       []
                       (:stock-list result-data))


   ;; Basic Long and Short Moving Averages
   :sma-list (reduce (fn [rslt ech]
                       (conj rslt [(first ech)
                                   (second ech)]))
                     []
                     (remove #(nil? (first %))
                             (:sma-list result-data)))

   :ema-list (reduce (fn [rslt ech]
                       (conj rslt [(first ech)
                                   (second ech)]))
                     []
                     (remove #(nil? (first %))
                             (:ema-list result-data)))


   ;; Bollinger-Band Data
   :bollinger-band (reduce (fn [rslt ech]
                             (conj rslt [(:last-trade-time ech)
                                         (:lower-band ech)
                                         (:upper-band ech)]))
                           []
                           (remove nil? (-> result-data :signals :bollinger-band)))

   ;; MACD Data
   :macd-price-list (reduce (fn [rslt ech]
                              (conj rslt [(:last-trade-time ech)
                                          (:last-trade-macd ech)]))
                            []
                            (remove nil? (-> result-data :signals :macd)))

   :macd-signal-list (reduce (fn [rslt ech]
                               (conj rslt [(:last-trade-time ech)
                                           (:ema-signal ech)]))
                             []
                             (remove nil? (-> result-data :signals :macd)))

   :macd-histogram-list (reduce (fn [rslt ech]
                                  (conj rslt [(:last-trade-time ech)
                                              (:histogram ech)]))
                                []
                                (remove nil? (-> result-data :signals :macd)))

   ;; Stochastic Oscillator
   :stochastic-k (reduce (fn [rslt ech]
                           (conj rslt [(:last-trade-time ech)
                                       (:K ech)]))
                         []
                         (remove nil? (-> result-data :signals :stochastic-oscillator)))

   :stochastic-d (reduce (fn [rslt ech]
                           (conj rslt [(:last-trade-time ech)
                                       (:D ech)]))
                         []
                         (remove nil? (-> result-data :signals :stochastic-oscillator)))

   :obv (reduce (fn [rslt ech]
                  (conj rslt [(:last-trade-time ech)
                              (:obv ech)]))
                []
                (remove nil? (-> result-data :signals :obv)))

   :signals {:moving-average (pull-out-signals result-data :moving-average)
             :bollinger-band (pull-out-signals result-data :bollinger-band)
             :macd (pull-out-signals result-data :macd)
             :stochastic-oscillator (pull-out-signals result-data :stochastic-oscillator)
             :obv (pull-out-signals result-data :obv)}

   :strategies {:strategy-A (pull-out-strategies result-data :strategy-A)
                :strategy-B (pull-out-strategies result-data :strategy-B)}

   :stock-name (:stock-name result-data)})
