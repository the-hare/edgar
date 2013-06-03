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
(require '[edgar.core.signal.lagging :as slagging])
(require '[edgar.core.signal.leading :as sleading])


(def live-list

  (map (fn [inp]

         (assoc inp
           :total-volume (read-string (:total-volume inp))
           :last-trade-size (read-string (:last-trade-size inp))
           :vwap (read-string (:vwap inp))
           :last-trade-price (read-string (:last-trade-price inp))))

       (read-string (slurp "etc/test-live-list.edn"))))


(def sma-list (alagging/simple-moving-average nil 20 live-list))
