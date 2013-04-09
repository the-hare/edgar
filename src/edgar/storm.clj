(ns edgar.storm)


;; STORM code
#_(defspout ibspout ["sentence"]
  [conf context collector]
  (let [sentences ["a little brown dog"
                   "the man petted the dog"
                   "four score and seven years ago"
                   "an apple a day keeps the doctor away"]]
    (spout
     (nextTuple []
                (Thread/sleep 100)
                (emit-spout! collector [(rand-nth sentences)]))
     (ack [id]
          ;; You only need to define this method for reliable spouts
          ;; (such as one that reads off of a queue like Kestrel)
          ;; This is an unreliable spout, so it does nothing here
          ))))


#_(defbolt ibbolt ["word"] [tuple collector]

  (let [words (.split (.getString tuple 0) " ")]
    (doseq [w words]
      (emit-bolt! collector [w] :anchor tuple))
    (ack! collector tuple)
  )
)


#_(defn mk-topology [in-spout]
  (storm/topology
   { "1" (storm/spout-spec in-spout) }
   { "3" (storm/bolt-spec  { "1" :shuffle }
                           ibbolt
                           :p 5)
     }))


#_(defn run-wib! []
  (let [connect-result (connect)
        contract (Contract. 0 "IBM" "STK" nil 0.0 nil nil "SMART" "USD" nil nil nil false nil nil)
        mdata (.reqMktData edgar.eclientsocket/esocket 0 contract nil false)

        xxx (println (str "*** Wrapper CREATION > " edgar.eclientsocket/wrap " ***"))
        my-spout (IBSpout. edgar.eclientsocket/wrap)

        cluster (LocalCluster.)]

    (.submitTopology cluster "ibbolt" {TOPOLOGY-DEBUG true} (mk-topology my-spout))
    (Thread/sleep 10000)
    (.shutdown cluster)))


#_(defn run-twitter! []
  (let [my-spout (TwitterSampleSpout. "twashing@gmail.com" "numd33nut5")
        cluster (LocalCluster.)]

    (.submitTopology cluster "ibbolt" {TOPOLOGY-DEBUG true} (mk-topology my-spout))
    (Thread/sleep 10000)
    (.shutdown cluster)))


(defn -main
  ([]
     (run-twitter!))
  ([ib]
     (run-wib!)))
