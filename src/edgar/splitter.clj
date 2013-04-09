(ns edgar.splitter

  (:use [clojure.repl])
  (:require [edgar.eclientsocket :as socket]
            [overtone.at-at :as at])
  (:gen-class
   :name edgar.splitter
   :methods [#^{:static true} [pushEvent [java.lang.Object]]])
  )


(defn connect []
  (socket/connect-to-tws))

(def my-pool (at/mk-pool))

(def event-list (ref ()))
(defn pushEvent
  "Push IB event data onto a Clojure list"
  [event]
  (dosync (alter event-list conj event) ))
(defn -pushEvent
  "A Java-callable wrapper around the 'pushEvent' function."
  [event]
  (pushEvent event))


(defn thing[]

  (connect)
  (at/every
   250
   (fn []

     (let [tuple (.getTuple socket/EWRAPPER)
           ]
       (doseq [tpl tuple]

         ;; ... check if stock exists in ThreadPool

         ;; ... if not, open a Thread that processes that stock

         )
       )

     )
   my-pool)

)
