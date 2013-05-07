(ns edgar.core.analysis.filter)

(defn simple-moving-average
  "Takes the tick-list, and moves back as far as the tick window will take it.

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."
  [tick-window tick-list]

  (let [;; calculate how far back the window can start
        start-index tick-window

        ;; back fill slots with nils, into an accompanying moving-average list
        ;;ma-list (into '() (repeat (- (count tick-list) tick-window) nil))
        ma-list (into '() (repeat tick-window nil))
        ]


    ;; calculate Simple Moving Average for each slot there's a window
    (reduce (fn [rslt ech]

              (let [tsum (reduce #(+ (:last-trade-price %)) ech)   ;; sum it up
                    taverage (/ tsum (count ech))   ;; get the average
                    ]

                (cons sma rslt)) )

            ma-list  ;; begin with a reversed tick-list, over which we can iterate
            (reverse (partition tick-window tick-list)))
    )
)
