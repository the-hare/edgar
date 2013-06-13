(ns ui.components
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]))



(defn populate-multiselect [selector options]

  (let-deferred [filtered-input ($/ajax "/list-filtered-input")]

                (let [multiselect ($ selector)]

                  (reduce (fn [rslt inp]

                            (let [option-value (second inp)
                                  option-label (nth inp 2)
                                  price-difference (.toFixed (first inp) 2)]

                              (-> multiselect
                                  (.append (str "<option value='" option-value "'>" option-label " (" price-difference ")</option>")))))
                          nil
                          (into-array (reader/read-string filtered-input)))

                  (-> ($ selector)
                      (.multiselect (clj->js (merge {:enableFiltering true} options)))))))
