(ns edgar.core.signal.confirming
  (require [edgar.core.analysis.confirming :as aconfirming]
           [edgar.core.signal.leading :as sleading]
           [edgar.core.signal.common :as common]))

(defn on-balance-volume
  "signal for the on-balance-volume analysis chart. This function uses.

   A. OBV Divergence from price."

  ([view-window tick-list]
     (let [obv-list (aconfirming/on-balance-volume (first tick-tick) tick-list)]
       (on-balance-volume view-window tick-list obv-list)))
  ([view-window tick-list obv-list]

     (let [partitioned-obv (partition view-window 1 obv-list)

           divergence-obv (reduce (fn [rslt ech-list]

                                     (let [fst (first ech-list)

                                           price-peaks-valleys (common/find-peaks-valleys nil ech-list)
                                           obv-peaks-valleys (common/find-peaks-valleys {:input :obv} ech-list)

                                           dUP? (divergence-up? nil  ech-list price-peaks-valleys obv-peaks-valleys)
                                           dDOWN? (divergence-down? nil ech-list price-peaks-valleys obv-peaks-valleys)]

                                       (if (or dUP? dDOWN?)

                                         (if dUP?
                                           (conj rslt (assoc fst :signals [{:signal :up
                                                                            :why :obv-divergence
                                                                            :arguments [ech-list price-peaks-valleys obv-peaks-valleys]
                                                                            :function divergence-up?}]))
                                           (conj rslt (assoc fst :signals [{:signal :down
                                                                            :why :obv-divergence
                                                                            :arguments [ech-list price-peaks-valleys obv-peaks-valleys]
                                                                            :function divergence-down?}])))
                                         (conj rslt (first ech-list)))))
                                   []
                                   partitioned-obv)]

       divergence-obv)))
