(ns edgar.tee.datomic
  )

(deftype DatomicTee

    (tee "Persist the bucket of stocks out to data store"
     [bucket]

     bucket
     )
  )
