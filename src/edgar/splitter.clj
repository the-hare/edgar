(ns edgar.splitter

  (:use [clojure.repl]
        [clojure.core.strint]
        [clojure.tools.namespace.repl])
  (:require [edgar.eclientsocket :as socket]
            [edgar.edgar :as edgar]
            [overtone.at-at :as at])
  (:gen-class
   :name edgar.splitter
   :methods [#^{:static true} [pushEvent [java.lang.Object]]])
  )


(def event-list (ref ()))

(defn pushEvent
  "Push IB event data onto a Clojure list"
  [event]

  (let [emap (into {} event)]
    (dosync (alter event-list conj emap) )))

(defn -pushEvent
  "A Java-callable wrapper around the 'pushEvent' function."
  [event]
  (pushEvent event))


(defn connect []
  (socket/connect-to-tws))

(def my-pool (at/mk-pool))


;; -------------
(defn test-publisher []
  (at/every 1000 (fn [] (dosync (alter event-list conj { :tickerId 0 :field 1 :price 5.75 :canAutoExecute 1}))) my-pool) )

;; -------------

(defn process-stock [stock-id]

  (let [remove-predicate #(= (% "tickerId") stock-id)
        local-list (ref ())
        ]

    (fn []

      (dosync (alter local-list concat (filter remove-predicate @event-list)))
      (dosync (alter event-list (fn [input-list] (remove remove-predicate input-list))))
      (println (str "___ EXEC thread > tickerId[" stock-id "] > local-list[" (list* @local-list) "]"))

      ))
  )
(defn fire-stock-processor [sid]

  (at/every
   250
   (process-stock sid)
   my-pool
   :desc sid)
 )


(defn get-market-data []

  (connect)
  (edgar/get-market-data)
  )
(defn process-events[]

  (at/every
   250
   (fn []

     (doseq [event @event-list]

       (println (<< "___ processing event[~{event}]"))

       ;; check if stock exists in ThreadPool
       (if (empty? (filter #(= (:desc %)
                               (.get event "tickerId"))
                           (at/scheduled-jobs my-pool)))


         ;; ** launch a new listener iff that stock is not already being listened to
         (fire-stock-processor (.get event "tickerId"))


         ;; ** otherwise ...

         )
       )
     )
   my-pool
   :desc "ib-event-processor")

)
