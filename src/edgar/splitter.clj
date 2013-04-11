(ns edgar.splitter

  (:use [clojure.repl]
        [clojure.core.strint])
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
  (dosync (alter event-list conj event) ))
(defn -pushEvent
  "A Java-callable wrapper around the 'pushEvent' function."
  [event]
  (pushEvent event))


(defn connect []
  (socket/connect-to-tws))
(def my-pool (at/mk-pool))


(defn process-stock [stock-id]

  (binding [remove-predicate #(= (:desc %) stock-id)
            local-list (ref ())
            ]

    (fn []

      (let [local-list (filter remove-predicate @event-list)
            ]

        (dosync (alter local-list conj ))
        (dosync (alter event-list remove remove-predicate))
        #_(println (<< "___ EXEC thread > tickerId[~{stock-id}]"))

        )
      ))
  )
(defn process-events[]

  (connect)
  (edgar/get-market-data)
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
         (do
           ;;(println (<< "___ processing stock event ... ~{event}"))
           (at/every
            250
            (process-stock (.get event "tickerId"))
            my-pool
            :initial-delay 0
            :desc (.get event "tickerId")))


         ;; ** otherwise ...
         ;;(println "___ stock already monitored. skip.")
         )
       )

     ;; flush out the event-list
     ;;(dosync (alter event-list empty))
     )
   my-pool
   :initial-delay 0
   :desc "ib-event-processor")

)
