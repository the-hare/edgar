(defn dev
  []
  (require 'dev)
  (in-ns 'dev)
  #_(dev/start))


(use 'clojure.tools.namespace.repl)
(use 'clojure.repl)
(require '[clojure.pprint :as pprint])
(require '[edgar.core.analysis.lagging :as alagging])

(def live-list (read-string (slurp "etc/test-live-list.edn")))
(def sma-list (alagging/simple-moving-average nil 20 live-list))

