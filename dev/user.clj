(defn dev
  []
  (require 'dev)
  (in-ns 'dev)
  #_(dev/start))


(use 'clojure.tools.namespace.repl)
(use 'clojure.repl)
(require '[clojure.pprint :as pprint])
(require '[edgar.core.analysis.lagging :as alagging])
(require '[edgar.core.analysis.leading :as aleading])
(require '[edgar.core.analysis.confirming :as aconfirming])
(require '[edgar.core.signal.lagging :as slagging])
(require '[edgar.core.signal.leading :as sleading])
(require '[edgar.core.signal.confirming :as sconfirming])
(require '[edgar.core.strategy.strategy :as strategy])


(def live-list
  (map (fn [inp]

         (assoc inp
           :total-volume (read-string (:total-volume inp))
           :last-trade-size (read-string (:last-trade-size inp))
           :vwap (read-string (:vwap inp))
           :last-trade-price (read-string (:last-trade-price inp))))
       (read-string (slurp "etc/test-live-list.edn"))))

(def sma-list (alagging/simple-moving-average nil 20 live-list))
(def ema-list (alagging/exponential-moving-average nil 20 live-list sma-list))
(def bollinger-list (alagging/bollinger-band 20 live-list sma-list))
(def macd-list (aleading/macd nil 20 live-list sma-list))
(def k-list (aleading/stochastic-oscillator 14 3 3 live-list))
(def obv-list (aconfirming/on-balance-volume (first live-list) live-list))

(def signals-ma (slagging/moving-averages 20 live-list sma-list ema-list))
(def signals-bollinger (slagging/bollinger-band 20 live-list sma-list))
(def signals-macd (sleading/macd nil 20 live-list sma-list))
(def signals-stochastic (sleading/stochastic-oscillator 14 3 3 live-list))
(def signals-obv (sconfirming/on-balance-volume 10 live-list))
