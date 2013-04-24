(ns edgar.tee.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [edgar.tee.tee :as tns]))

(deftype DatomicTee [] tns/Tee

    (tee [bucket]

      ;; parse seed data dtm file
      ;;(def data-tx (read-string (slurp "samples/seattle/seattle-data0.dtm")))

      ;; display first three statements in seed data transaction
      ;;(first data-tx)
      ;;(second data-tx)
      ;;(nth data-tx 2)

      ;; submit seed data transaction
      @(d/transact conn data-tx)


     )
  )
