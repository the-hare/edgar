(ns edgar.core.analysis.filter)

(defn simple-moving-average
  "Takes the tick-list, and moves back as far as the tick window will take it.

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."
  [tick-window tick-list]

  (let [;; calculate how far back the window can start
        start-index tick-window

        ;; back fill slots with nils, into an accompanying moving-average list
        ma-list (into '() (repeat tick-window nil))
        ]


    ;; calculate Simple Moving Average for each slot there's a window
    (reduce (fn [rslt ech]

              (let [tsum (reduce (fn [rslt inp]

                                   (let [ltprice (:last-trade-price inp)]
                                     (+ (if (string? ltprice) (read-string ltprice) ltprice) rslt))) 0 ech)   ;; sum it up
                    taverage (/ tsum (count ech))   ;; get the average
                    ]

                (cons {:last-trade-price-average taverage :last-trade-time (:last-trade-time (first ech))} rslt)) )

            ma-list  ;; begin with a reversed tick-list, over which we can iterate
            (reverse (partition tick-window 1 tick-list)))
    )
)
