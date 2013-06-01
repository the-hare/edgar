(ns edgar.core.signal.leading
  (:require [edgar.core.analysis.leading :as lead-analysis]
            [edgar.core.analysis.lagging :as lag-analysis]))


(defn macd
  "Functions searches for signals to overlay on top of a regular MACD time series. It uses the following strategies

   A. MACD / signal crossover

   B. MACD / RSI divergence

   C. MACD Stairsteps
  "

  ([options tick-window tick-list]
     (macd options tick-window tick-list (lag-analysis/simple-moving-average nil tick-window tick-list)))

  ([options tick-window tick-list sma-list]

     ;; A.

     ;; B.

     ;; C.

     )
)
