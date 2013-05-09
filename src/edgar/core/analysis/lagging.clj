(ns edgar.core.analysis.lagging
  (:require #_[clojure.pprint :as pprint]))

(defn simple-moving-average
  "Takes the tick-list, and moves back as far as the tick window will take it.

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."
  [tick-window tick-list]

  (let [;; calculate how far back the window can start
        start-index tick-window

        ;; back fill slots with nils, into an accompanying moving-average list
        ma-list (into '() (repeat tick-window nil))]


    ;; calculate Simple Moving Average for each slot there's a window
    (reduce (fn [rslt ech]

              (let [tsum (reduce (fn [rslt inp]
                                   (let [ltprice (:last-trade-price inp)]
                                     (+ (if (string? ltprice) (read-string ltprice) ltprice) rslt))) 0 ech)   ;; sum it up
                    taverage (/ tsum (count ech))   ;; get the average
                    ]

                (cons {:last-trade-price (:last-trade-price (first ech))
                       :last-trade-price-average taverage
                       :last-trade-time (:last-trade-time (first ech))
                       :population ech} rslt)))

            ma-list  ;; begin with a reversed tick-list, over which we can iterate
            (reverse (partition tick-window 1 tick-list)))
    )
  )

(defn exponential-moving-average
  "From a tick-list, generates an accompanying exponential moving average list.

     EMA = price(today) * k + EMA(yesterday) * (1 - k)
     k = 2 / N + 1
     N = number of days

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."

  ([tick-window tick-list]

     (exponential-moving-average tick-window tick-list (simple-moving-average tick-window tick-list)))

  ([tick-window tick-list sma-list]

     ;; 1. calculate 'k'
     ;; k = 2 / N + 1
     ;; N = number of days
     (let [k (/ 2 (+ tick-window 1))
           ema-list (into '() (repeat tick-window nil))]


       ;; 2. get the simple-moving-average for a given tick - 1
       (reduce (fn [rslt ech]

                 ;; 3. calculate the EMA ( for the first tick, EMA(yesterday) = MA(yesterday) )

                 (let [;; price(today)
                       ltprice (:last-trade-price ech)

                       ;; EMA(yesterday)
                       ema-last (if (:last-trade-price-exponential (first rslt))
                                  (:last-trade-price-exponential (first rslt))
                                  (:last-trade-price ech))

                       ;; ** EMA now = price(today) * k + EMA(yesterday) * (1 - k)
                       ema-now (+ (* k (if (string? ltprice)
                                         (read-string ltprice)
                                         ltprice))
                                  (* (if (string? ema-last) (read-string ema-last) ema-last) (- 1 k)))]

                   (cons {:last-trade-price (:last-trade-price ech)
                          :last-trade-time (:last-trade-time ech)
                          :last-trade-price-exponential ema-now} rslt)))
               ema-list
               (->> sma-list (remove nil?) reverse)))
     )
  )

(defn bollinger-band
  "From a tick-list, generates an accompanying list with upper-band and lower-band

     Upper Band: K times an N-period standard deviation above the moving average (MA + Kσ)
     Lower Band: K times an N-period standard deviation below the moving average (MA − Kσ)
     K: number of standard deviations
     N: period, or tick-window we are looking at

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."

  ([tick-window tick-list]
     (bollinger-band tick-window tick-list (simple-moving-average tick-window tick-list)))

  ([tick-window tick-list sma-list]

     ;; At each step, the Standard Deviation will be: the square root of the variance (average of the squared differences from the Mean)

     (let [bollinger-list (into '() (repeat tick-window nil))]

       (reduce (fn [rslt ech]

                 (let [;; get the Moving Average
                       ma (:last-trade-price-average ech)

                       ;; work out the mean
                       mean (/ (reduce (fn [rslt ech] (+ (:last-trade-price ech) rslt))
                                       0
                                       (:population ech))
                               (count (:population ech)))

                       ;; Then for each number: subtract the mean and square the result (the squared difference)
                       sq-diff-list (map (fn [ech]
                                           (let [diff (- mean (:last-trade-price ech))]
                                             (* diff diff)
                                             ))
                                         (:population ech))

                       variance (/ (reduce + sq-diff-list) (count (:population ech)))
                       standard-deviation (. Math sqrt variance)
                       ]
                   (cons {:last-trade-price (:last-trade-price ech)
                          :last-trade-time (:last-trade-time ech)
                          :upper-band (+ ma (* 2 standard-deviation))
                          :lower-band (- ma (* 2 standard-deviation))
                          }
                         rslt)
                   )
                 )
               bollinger-list
               (->> sma-list (remove nil?) reverse)))
     )
  )
