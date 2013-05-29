(ns edgar.core.signal.lagging-test
  (:use [midje.sweet])
  (:require [edgar.core.analysis.lagging :as analysis]
            [edgar.core.signal.lagging :as signal]))


(let [live-list (read-string (slurp "etc/test-live-list.edn"))
      sma-list (analysis/simple-moving-average nil 20 live-list)
      ema-list (analysis/exponential-moving-average nil 20 live-list sma-list)]

  (fact "Testing basic crossove signal"
        (signal/moving-averages live-list sma-list ema-list) => 1))
