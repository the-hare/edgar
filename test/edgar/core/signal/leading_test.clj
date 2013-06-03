(ns edgar.core.signal.leading-test
  (:use [midje.sweet])
  (:require [edgar.core.analysis.lagging :as alagging]
            [edgar.core.analysis.leading :as aleading]
            [edgar.core.signal.leading :as sleading]))



(let [live-list (read-string (slurp "etc/test-live-list.edn"))
      sma-list (alagging/simple-moving-average nil 20 live-list)

      signals-macd (sleading/macd nil 20 live-list sma-list)]

  (fact "Testing macd signals"
        2 => 2))
