(ns edgar.core.tee.live

  (:require [edgar.ib.market :as market]
            [edgar.core.edgar :as edgar]
            [edgar.core.analysis.lagging :as alagging]
            [edgar.core.signal.common :as common]
            [edgar.core.signal.lagging :as slagging]
            [edgar.core.signal.leading :as sleading]
            [edgar.core.signal.confirming :as sconfirming]
            [edgar.core.strategy.strategy :as strategy]
            [edgar.core.strategy.target :as target]
            ))


(def tracking-data (ref []))
(def ^:dynamic *position-data* (ref []))
(def ^:dynamic *orderid-index* (ref 0))

(defn track-strategies
  "Follows new strategy recommendations coming in"
  [tick-list strategy-list]

  ;; iterate through list of strategies
  (reduce (fn [rA eA]

            #_(println (str "... 1 > eA[" eA "] > some test if/else[" (some #(= % (:tickerId eA)) (map :tickerId @tracking-data)) "]"))
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
                                                          [(first (map first (seq result-filter)))]
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

(defn watch-strategies
  "Tracks and instruments existing strategies in play"
  [tick-list]

  #_(println (str "... 1 > WATCH > watch-strategies > test[" (some #(= % (:tickerId (first tick-list)))
                                                 (map :tickerId @tracking-data)) "]"))

  ;; check if latest tick matches a stock being watched
  (if (some #(= % (:tickerId (first tick-list)))
            (map :tickerId @tracking-data))

    (dosync (alter tracking-data (fn [inp]

                                   (let [result-filter (filter #(= (-> % second :tickerId) (:tickerId (first tick-list)))
                                                               (map-indexed (fn [idx itm] [idx itm]) inp))]

                                     #_(println (str "... 2 > WATCH > result-filter[" (into [] result-filter) "] / integer key[" (first (map first result-filter)) "] / inp[" (into [] inp) "]"))

                                     ;; update-in-place, the existing tracking-data
                                     ;; i. find index of relevent entry
                                     (update-in (into [] inp)
                                                [(first (map first (into [] result-filter)))]
                                                (fn [i1]

                                                  #_(println (str "... 3 > WATCH > update-in > inp[" i1 "]"))
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

                                                    #_(println (str "... 4 > WATCH > result[" merge-result "]"))
                                                    merge-result)))))))))

(defn trim-strategies [tracking-data tick-list]

  (println (str "... trim-strategies / SELL test[" (some #(= :down (-> % :action :action)) @tracking-data)  "] / ACTION[" (seq (filter #(= :down (-> % :action :action)) @tracking-data)) "] / WHY[" (:why (first (filter #(= :down (-> % :action)) @tracking-data))) "]"))
  (dosync (alter tracking-data
                 (fn [inp]
                   (remove #(= :down (-> % :action :action))
                           inp)))))


(defn tee-fn [output-fn tick-list]

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

        #_sA #_(if (empty? @tracking-data)

                 [(assoc (first tick-list-N) :strategies [{:signal :up
                                                           :name :strategy-test-A
                                                                                                    :why "test-b"}])]
                 [])

        sB (strategy/strategy-B tick-list-N
                                signals-ma
                                signals-bollinger
                                signals-macd
                                signals-stochastic
                                signals-obv)
        #_sB #_(if (empty? @tracking-data)

                 [(assoc (first tick-list-N) :strategies [{:signal :up
                                                           :name :strategy-test-B
                                                           :why "test-b"}])]
                 [])

        ;; TODO... track the stock-name
        result-data {:stock-name "TDB" #_stock-name
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
    (println (str "... 0 > tracking-data[" (seq @tracking-data) "]"))
    (println (str "... strategy-A[" sA "] / strategy-B[" sB "] / test[" (or (not (empty? sA))
                                                                            (not (empty? sB)))"]"))


    (if (or (not (empty? sA))
            (not (empty? sB)))


      ;; track any STRATEGIES
      (track-strategies tick-list-N (remove nil? [(first sA) (first sB)]))


      ;; watch any STRATEGIES in play
      (if (not (empty? @tracking-data))
        (watch-strategies tick-list-N)))


    ;; ORDER based on tracking data
    (let [client (:interactive-brokers-client edgar/*interactive-brokers-workbench*)
            tick (first @tracking-data)]

      (if (some #(= :up (-> % :action :action)) @tracking-data)

        (do

          ;; ... TODO: make sure we don't double-buy yet
          ;; ... TODO: track orderId for sale
          ;; ... TODO: stock-symbol has to be tied to the tickerId
          ;; (market/buy-stock client *order-id* stock-symbol 100 (:last-trade-price tick))
          (swap! @*orderid-index* inc))

        (if (some #(= :down (-> % :action :action)) @tracking-data)

          123
          ;; (market/sell-stock client *order-id* stock-symbol 100 (:last-trade-price tick))
          ))
      )



    ;; remove tracked stock if sell
    (if (not (empty? @tracking-data))
      (trim-strategies tracking-data tick-list-N))

    (output-fn "stream-live" result-data))
  []
  tick-list)
