(ns edgar.core.signal.leading
  (:require [edgar.core.analysis.leading :as lead-analysis]
            [edgar.core.analysis.lagging :as lag-analysis]))


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

                     (if macd-cross-abouve?
                       (conj rslt (assoc fst :signal {:signal :up
                                                      :why :macd-signal-crossover
                                                      :population ech
                                                      :function macd-cross-abouve?}))
                       (conj rslt (assoc fst :signal {:signal :down
                                                      :why :macd-signal-crossover
                                                      :population ech
                                                      :function macd-cross-below?})))
                     (conj rslt fst))))
               []
               partitioned-list)))


(defn macd
  "Functions searches for signals to overlay on top of a regular MACD time series. It uses the following strategies

   A. MACD / signal crossover
      when i. MACD line crosses over the ii. signal line

   B. MACD divergence
      when i. closing price makes a higher high and ii. MACD makes a lower high
      when price rises and falls quickly

      OR

      look for high price resistance over last 3 peaks
      when i. closing price makes a higher high and ii. histogram makes a lower high

      after both are true, look for

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


           ;; C.


           ]
     )
))
