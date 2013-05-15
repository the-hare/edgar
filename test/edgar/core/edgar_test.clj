(ns edgar.core.edgar-test
  (:use [midje.sweet])
  (:require [edgar.core.edgar :as edgar]))


(fact "first test"
      (edgar/fn-under-test 1 2 3) => 6)
