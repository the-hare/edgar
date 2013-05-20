(ns edgar
  (:use [jayq.core :only [$ css inner]]))


;; Scrolling with Lionbars
(.lionbars ($ ".tab-content"))

(-> ($ "#tab-one-graph")
    (.highcharts "StockChart" (clj->js
                               {:rangeSelector {:selected 1},
                                :title {:text "Test Stock Data"},
                                :series [{:name "Test Stock Data",
                                          :data tick-list,
                                          :marker {:enabled true, :radius 3},
                                          :shadow true,
                                          :tooltip {:valueDecimals 2}}]})
                 ))
