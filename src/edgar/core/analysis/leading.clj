(ns edgar.core.analysis.leading
  (:require [edgar.core.analysis.lagging :as lagging]))


(defn macd
  "The MACD 'oscillator' or 'indicator' is a collection of three signals (or computed data-series), calculated from historical price data. These three signal lines are:

    i) the MACD line: difference between the 12 and 26 days EMAs
      MACD = EMA[stockPrices,12] – EMA[stockPrices,26]

    ii) the signal line (or average line): 9 EMA of the MACD line
      signal = EMA[MACD,9]

    iii) and the difference (or divergence): difference between the blue and red lines
      histogram = MACD – signal

    Options are:
      :macd-window-fast (default is 12)
      :macd-window-slow (default is 26)
      :signal-window (default is 9)"

  ([options tick-window tick-list]
     (macd options tick-window tick-list (lagging/simple-moving-average nil tick-window tick-list)))

  ([options tick-window tick-list sma-list]

     ;; compute the MACD line
     (let [
           ;; 1. compute 12 EMA
           ema-short (lagging/exponential-moving-average nil 12 tick-list sma-list)


           ;; 2. compute 26 EMA
           ema-long (lagging/exponential-moving-average nil 26 tick-list sma-list)


           ;; 3. for each tick, compute difference between 12 and 26 EMA
           ;; EMA lists will have a structure like:
           #_({:last-trade-price 203.98,
                :last-trade-time 1368215573010,
                :last-trade-price-exponential 204.00119130504845}
               )

           macd (map (fn [e1 e2]

                       (if (and (-> e1 nil? not)
                                (-> e2 nil? not))

                         {:last-trade-price (:last-trade-price e1)
                          :last-trade-time (:last-trade-time e1)
                          :last-trade-macd (- (:last-trade-price-exponential e1) (:last-trade-price-exponential e2))
                          }))
                     ema-short
                     ema-long)

           ;; Compute 9 EMA of the MACD
           ;;ema-signal (lagging/exponential-moving-average {:input :last-trade-macd :output :ema-signal :etal [:last-trade-price :last-trade-time]} 9 macd)

           ]

       ;; compute the difference, or divergence
       macd

       )
     )
)
