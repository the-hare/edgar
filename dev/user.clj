(defn dev
  []
  (require 'dev)
  (in-ns 'dev)
  #_(dev/start))


(use 'clojure.tools.namespace.repl)
(use 'clojure.repl)
(require '[clojure.pprint :as pprint])


