(ns edgar.core.signal.leading
  (:require [edgar.core.analysis.leading :as lanalysis]))


(defn macd
  "Functions searches for signals to overlay on top of a regular MACD time series. It uses the following strategies

   A. MACD / signal crossover

   B. MACD / RSI divergence

   C. MACD Stairsteps
  "

  ([options tick-window tick-list]
     (macd options tick-window tick-list (lagging/simple-moving-average nil tick-window tick-list)))

  ;; A.

  ;; B.

  ;; C.

  )
