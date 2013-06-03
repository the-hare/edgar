(ns edgar.core.signal.leading
  (:require [edgar.core.analysis.leading :as lead-analysis]
            [edgar.core.analysis.lagging :as lag-analysis]
            [edgar.core.signal.common :as common]))


(defn macd-cross-abouve? [fst snd]
  (and (< (:last-trade-macd snd) (:ema-signal snd))
       (> (:last-trade-macd fst) (:ema-signal snd))))
(defn macd-cross-below? [fst snd]
  (and (> (:last-trade-macd snd) (:ema-signal snd))
       (< (:last-trade-macd fst) (:ema-signal fst))))

(defn macd-signal-crossover [macd-list]

  (let [partitioned-list (partition 2 1 (remove nil? macd-list))]

       (reduce (fn [rslt ech]

                 (let [fst (first ech)
                       snd (second ech)

                       macd-cross-A? (macd-cross-abouve? fst snd)
                       macd-cross-B? (macd-cross-below? fst snd)]

                   (if (or macd-cross-A? macd-cross-B?)

                     (if macd-cross-A?
                       (conj rslt (assoc fst :signal {:signal :up
                                                      :why :macd-signal-crossover
                                                      :arguments [ech]
                                                      :function macd-cross-abouve?}))
                       (conj rslt (assoc fst :signal {:signal :down
                                                      :why :macd-signal-crossover
                                                      :arguments [ech]
                                                      :function macd-cross-below?})))
                     (conj rslt fst))))
               []
               partitioned-list)))

(defn divergence-up? [ech-list price-peaks-valleys macd-peaks-valleys]

  (let [price-higher-high? (> (:last-trade-price (first ech-list)) (:last-trade-price (first price-peaks-valleys)))
        macd-lower-high? (< (:last-trade-macd (first ech-list)) (:last-trade-macd (first macd-peaks-valleys)))]

    (and price-higher-high? macd-lower-high?)))

(defn divergence-down? [ech-list price-peaks-valleys macd-peaks-valleys]

  (let [price-lower-high? (< (:last-trade-price (first ech-list)) (:last-trade-price (first price-peaks-valleys)))
        macd-higher-high? (> (:last-trade-macd (first ech-list)) (:last-trade-macd (first macd-peaks-valleys)))]

    (and price-lower-high? macd-higher-high?)))

(defn macd-divergence [view-window macd-list]

  (let [partitioned-macd (partition view-window 1 macd-list)


        ;; B i.
        ;;    when i. closing price makes a higher high and ii. MACD makes a lower high
        ;;    ... TODO - when price rises and falls quickly
        divergence-macd (reduce (fn [rslt ech-list]

                                  (let [fst (first ech-list)

                                        price-peaks-valleys (common/find-peaks-valleys nil ech-list)
                                        macd-peaks-valleys (common/find-peaks-valleys {:input :last-trade-macd} ech-list)

                                        dUP? (divergence-up? ech-list price-peaks-valleys macd-peaks-valleys)
                                        dDOWN? (divergence-down? ech-list price-peaks-valleys macd-peaks-valleys)
                                        ]

                                    (if (or dUP? dDOWN?)

                                      (if dUP?
                                        (conj rslt (assoc fst :signal {:signal :up
                                                                       :why :mac-divergence
                                                                       :arguments [ech-list price-peaks-valleys macd-peaks-valleys]
                                                                       :function divergence-up?}))
                                        (conj rslt (assoc fst :signal {:signal :down
                                                                       :why :macd-divergence
                                                                       :arguments [ech-list price-peaks-valleys macd-peaks-valleys]
                                                                       :function divergence-down?})))
                                      (conj rslt (first ech-list)))))
                                []
                                partitioned-macd)

        ;; B ii. ... TODO - if histogram goes into negative territory

        ]

    )

  )


(defn macd
  "Functions searches for signals to overlay on top of a regular MACD time series. It uses the following strategies

   A. MACD / signal crossover
      when i. MACD line crosses over the ii. signal line

   B. MACD divergence

      i)
      when i. closing price makes a higher high and ii. MACD makes a lower high
      ... TODO - when price rises and falls quickly

      OR

      ii)
      look for high price resistance over last 3 peaks
      when i. closing price makes a higher high and ii. histogram makes a lower high

      ... TODO - after both are true, look for
         i. subsequent 3 closing prices to be below the high

         OR

         ii. if histogram goes into negative territory

   ... TODO - C. MACD Stairsteps (http://www.youtube.com/watch?v=L-cB_zZcpks)

      ENTRY:
         when i. MACD crosses over ii. the signal line
         when subsequent 3 low(s) are equal or greater than the previous high(s)

      EXIT:
         measure last up-move and project target (difference from last high, from low); stop below the current low
  "

  ([options tick-window tick-list]
     (macd options tick-window tick-list (lag-analysis/simple-moving-average nil tick-window tick-list)))

  ([options tick-window tick-list sma-list]
     (let [macd-list (lead-analysis/macd options tick-window tick-list sma-list)]
       (macd options tick-window tick-list sma-list macd-list)))

  ([options tick-window tick-list sma-list macd-list]


     (let [

           ;; A.
           macd-A (macd-signal-crossover macd-list)


           ;; B.
           macd-B (macd-divergence 10 macd-list)

           ;; C.


           ]
     )
))
