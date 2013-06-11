(ns edgar.core.strategy.strategy
  )


(defn price-increase? [tick-list]

  (let [fst (first tick-list)
        snd (second tick-list)]

    (> (:last-trade-price fst) (:last-trade-price snd))))

(defn price-below-sma? [tick-list signals-ma]

  (let [p-tick (first tick-list)
        sma-tick (first signals-ma)]

    (< (:last-trade-price p-tick) (:last-trade-price-average sma-tick))))

(defn bollinger-price-below? [tick-list signals-bollinger]

  (let [p-tick (first tick-list)
        b-ticks (take 2 signals-bollinger)]

    (some (fn [inp]
            (<= (:last-trade-price p-tick)
                (:lower-band inp)))
          b-ticks)))

(defn bollinger-was-narrower? [signals-bollinger]

  (let [b1 (first signals-bollinger)
        b-first (assoc b1 :difference (- (:upper-band b1)
                                         (:lower-band b1)))

        b2 (take 2 (rest signals-bollinger))
        b-rest (map (fn [inp]
                      (assoc inp :difference (- (:upper-band inp)
                                                (:lower-band inp))))
                    b2)]

    (some (fn [inp]
            (> (:difference b-first)
               (:difference inp)))
          b-rest)))

(defn macd-histogram-squeeze? [signals-macd]

  (let [h-first (first signals-macd)
        h-rest (take 2 (rest signals-macd))]

    (some (fn [inp]
            (> (:histogram h-first)
               (:histogram inp)))
          h-rest)))

(defn obv-increasing? [signals-obv]

  (let [fst (first signals-obv)
        snd (second signals-obv)]

    (> (:obv fst) (:obv snd))))


(defn strategy-A
  "This strategy is a composition of the following signals:

   A. Price increase
   B. Price below the SMA

   C. Price was just at or below the bollinger-band (w/in last 2 ticks)
   D. Bollinger-Band was narrower (w/in last 2 ticks)

   E. MACD Histogram squeeze

   F. OBV increasing

   G. Stochastic is oversold, or was (w/in last 2 ticks)"
  [tick-list signals-ma signals-bollinger signals-macd signals-stochastic signals-obv]

  (let [
        ;; A.
        price-increaseV (price-increase? tick-list)

        ;; B.
        price-below-smaV (price-below-sma? tick-list signals-ma)

        ;; C.
        bollinger-price-belowV (bollinger-price-below? tick-list signals-bollinger)

        ;; D.
        bollinger-was-narrowerV (bollinger-was-narrower? signals-bollinger)

        ;; E.
        macd-histogram-squeezeV (macd-histogram-squeeze? signals-macd)

        ;; F.
        obv-increasingV (obv-increasing? signals-obv)]

    ))


(defn strategy-B
  "This strategy is a composition of the following signals:

   A. Price crosses abouve SMA

   B. Bollinger-Band was narrower (w/in last 2 ticks)

   C. MACD crossover

   D. Stochastic crossover
   E. Stochastic is oversold or was (w/in last 2 ticks)

   F. OBV increasing"
  [tick-list signals-ma signals-bollinger signals-macd signals-stochastic signals-obv]

  nil)