(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:use-macros [jayq.macros :only [let-ajax let-deferred]])
  (:require [jayq.core :as jq]))


;; === SCROLLING  with Lionbars
#_(.lionbars ($ ".body-container"))



;; === RENDER the Live stock graph
(defn render-stock-graph [selector tlist label]

  (-> ($ selector)
      (.highcharts "StockChart" (clj->js
                                 {:rangeSelector {:selected 1},
                                  :title {:text label},
                                  :series [{:name label,
                                            :data tlist
                                            :marker {:enabled true, :radius 3},
                                            :shadow true,
                                            :tooltip {:valueDecimals 2}}]})
                   )))

(def tick-list (clj->js [[1368215573010 203.98] [1368215576331 203.99] [1368215576857 203.99] [1368215577765 203.99] [1368215578769 204.0] [1368215579272 204.01] [1368215579517 204.02] [1368215581769 204.02] [1368215583602 204.01] [1368215585650 204.02] [1368215586060 204.02] [1368215587029 204.01] [1368215588318 204.02] [1368215589335 204.01] [1368215589536 204.01] [1368215589846 204.0] [1368215591079 203.99] [1368215591789 203.99] [1368215592104 203.98] [1368215592615 203.98] [1368215592758 203.99] [1368215594039 203.97] [1368215597119 203.98] [1368215597632 203.97] [1368215599396 203.97] [1368215603876 203.96] [1368215606059 203.96] [1368215610316 203.95] [1368215610634 203.95] [1368215610813 203.93] [1368215612886 203.95] [1368215615858 203.94] [1368215618621 203.94] [1368215619138 203.96] [1368215623846 203.94] [1368215632669 203.94] [1368215634709 203.92] [1368215636587 203.93] [1368215636952 203.94] [1368215638328 203.93]]))

(render-stock-graph "#live-stock-graph" tick-list "IBM")
(render-stock-graph "#historical-stock-graph" tick-list "AAPL")



;; === POPULATE the live multi-select
(defn populate-multiselect [selector options]

  (let-deferred
   [filtered-input ($/ajax "/list-filtered-input")]

   (let [multiselect ($ selector)]

     (reduce (fn [rslt inp]

               (let [option-value (second inp)
                     option-label (nth inp 2)
                     price-difference (.toFixed (first inp) 2)]

                 (-> multiselect
                     (.append (str "<option value='" option-value "'>" option-label " (" price-difference ")</option>")))))
             nil
             (into-array filtered-input))

     (-> ($ selector)
         (.multiselect (clj->js (merge {:enableFiltering true} options))))))

  )

(populate-multiselect ".multiselect-live" {#_:buttonText #_(fn [options]
                                                               (if (= 0 (.length options))
                                                                 "Historical Selections"))
                                                 :onChange (fn [element checked]
                                                             (if checked

                                                               ($/post "/get-streaming-stock-data"
                                                                       (clj->js {:complete (fn [jqXHR status]
                                                                                             (.log js/console (str "POST:: get-streaming-stock-data > jqXHR[" jqXHR "] / status[" status "]")))})))
                                                             )})


(populate-multiselect ".multiselect-historical" {#_:buttonText #_(fn [options]
                                                         (if (= 0 (.length options))
                                                           "Live Selections"))
                                           :onChange (fn [element checked]
                                                       (if checked
                                                         ($/ajax "/get-historical-data"
                                                                 (clj->js {:data {:stock-selection (.val element)
                                                                                  :time-duration "60 S"
                                                                                  :time-interval "1 secs"}
                                                                           :complete (fn [jqXHR status]
                                                                                       (.log js/console (str ".multiselect-historical > jqXHR[" jqXHR "] / status[" status "]"))
                                                                                       )})))
                                                       )})

(->
 ($ "#live-initialize")
 (.click (fn [arg1 arg2]

           (def livesource (js/window.EventSource. (str "/get-streaming-stock-data")))
           (.addEventListener livesource
                              "stream-live"
                              (fn [e]
                                (let [data (r/read-string (.-data e))]
                                  (.log js/console (str ">> stream-live[" e "]"))))
                              false)
           )))
