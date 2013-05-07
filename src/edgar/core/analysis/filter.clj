(ns edgar.core.analysis.filter)

(defn simple-moving-average
  "Takes the tick-list, and moves back as far as the tick window will take it.

   Returns a list, equal in length to the tick-list, but only with slots filled,
   where preceding tick-list allows."
  [tick-window tick-list]

  ;; make an accompanying moving-average list
  (def ma-list '())

  ;; calculate how far back the window can start
  (def start-index tick-window)

  ;; back fill slots with nils
  (into ma-list (repeat (- tick-window (count tick-list)) nil))


  ;; calculate Simple Moving Average for each slot there's a window

  (take 20 tick-list))
