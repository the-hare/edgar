(ns edgar.scheduler
  (:require [overtone.at-at :as at])
  )


(defn initialize-pool
  "Initialize the thread pool"
  []

  (defonce my-pool (at/mk-pool)))

(defn schedule-task
  "Scedule a task to be performed. Options (and defaults) are {:msec 1000 :sec 60 :min 1}"
  [options task-fn]

  (let [opts (merge {:msec 1000 :sec 60 :min 1} options)
        msec (:msec opts)
        sec (:sec opts)
        min (:min opts)
        ]

    (at/every
     (* min sec msec)
     task-fn
     my-pool))
  )
