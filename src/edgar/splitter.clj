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
(defn process-events[]

  (connect)
  (edgar/get-market-data)
  (at/every
   250
   (fn []

     (doseq [event @event-list]

       (println (<< "processing event ... ~{event}"))

       ;; ... check if stock exists in ThreadPool
       (let [all-jobs (at/scheduled-jobs my-pool)
             ]


         (if (empty? (filter #(= (:desc %)
                                 (.get event "tickerId"))
                             all-jobs))

           ;; launch a new listener iff that stock is not already being listened to
           (at/every
            250

            (fn []
              (println (<< "___ EXEC thread > tickerId[~{}]")))

            my-pool
            :initial-delay 0
            :desc (.get event "tickerId"))

           ;; otherwise ...
           (println "___ list was empty. skip.")
           ))

       )

     ;; flush out the event-list
     (dosync (alter event-list empty))
     )
   my-pool
   :initial-delay 0
   :desc "ib-event-processor")

)
