(ns edgar.core.analysis.leading
  (:require '[edgar.core.analysis.lagging :as lagging]))


(defn macd
  "The MACD 'oscillator' or 'indicator' is a collection of three signals (or computed data-series), calculated from historical price data. These three signal lines are:

    i) the MACD line: difference between the 12 and 26 days EMAs
      MACD = EMA[stockPrices,12] – EMA[stockPrices,26]

    ii) the signal line (or average line): 9 day EMA of the MACD line
      signal = EMA[MACD,9]

    iii) and the difference (or divergence): difference between the blue and red lines
      histogram = MACD – signal"

  ([tick-window tick-list]
     (macd tick-window tick-list (lagging/simple-moving-average tick-window tick-list)))

  ([tick-window tick-list sma-list]

     )
  )
