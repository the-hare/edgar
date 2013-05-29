(ns edgar.core.signal.lagging
  (:require [edgar.core.analysis.lagging :as analysis]))


(defn moving-averages
  "Takes baseline time series, along with 2 other moving averages.

   Produces a list of signals where the 2nd moving average overlaps (abouve or below) the first.
   By default, this function will produce a Simple Moving Average and an Exponential Moving Average."

  ([tick-window tick-list]

     (let [sma-list (analysis/simple-moving-average nil tick-window tick-list)
           ema-list (analysis/exponential-moving-average nil tick-window tick-list sma-list)]
       (moving-averages tick-window tick-list sma-list ema-list)))

  ([tick-window tick-list sma-list ema-list]

     ;; create a list where i) tick-list ii) sma-list and iii) ema-list are overlaid

     (let [joined-list (map (fn [titem sitem eitem]


                              #_(println (str "... titem[" (:last-trade-time titem) "] sitem[" (:last-trade-time sitem) "] eitem[" (:last-trade-time eitem)
                                               "] > MATCH[" (= (:last-trade-time titem) (:last-trade-time sitem) (:last-trade-time eitem)) "]"))

                              ;; 1. ensure that we have the :last-trade-time for simple and exponential items
                              ;; 2. ensure that all 3 time items line up
                              (if (and (and (not (nil? (:last-trade-time sitem)))
                                            (not (nil? (:last-trade-time eitem))))
                                       (= (:last-trade-time titem) (:last-trade-time sitem) (:last-trade-time eitem)))


                                {:last-trade-time (:last-trade-time titem)
                                 :last-trade-price (:last-trade-price titem)
                                 :last-trade-price-average (:last-trade-price-average sitem)
                                 :last-trade-price-exponential (:last-trade-price-exponential eitem)}

                                nil))

                            tick-list
                            sma-list
                            ema-list)]

       joined-list)
     ;; find time points where ema-list (or second list) crosses over the sma-list (or 1st list)

     ))
