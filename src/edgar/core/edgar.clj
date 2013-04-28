(ns edgar.core.edgar
  (:use [clojure.repl]
        [clojure.core.strint]
        [datomic.api :only [q db] :as d])
  )


(defn asdf []

  ;; find entity.symbol (and entire entity)
  ;; where price-difference is greatest
  ;;(q '[:find ?c :where [?c :community/name]] (db conn))

  )
