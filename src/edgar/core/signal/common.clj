(ns edgar.core.signal.common)


(defn find-peaks-valleys [tick-list]
  (reduce (fn [rslt ech]
            (let [fst (read-string (:last-trade-price (first ech)))
                  snd (read-string (:last-trade-price (second ech)))
                  thd (read-string (:last-trade-price (nth ech 2)))
                  valley? (and (> fst snd) (< snd thd))
                  peak? (and (< fst snd) (> snd thd))]

              (if (or valley? peak?)
                (if peak?
                  (conj rslt (assoc (second ech) :signal :peak))
                  (conj rslt (assoc (second ech) :signal :valley)))
                rslt)))
          []
          (partition 3 1 tick-list)))


(defn up-market? [period partitioned-list]
  (every? (fn [inp]
            (> (read-string (:last-trade-price (first inp)))
               (read-string (:last-trade-price (second inp)))))
          (take period partitioned-list)))


(defn down-market? [period partitioned-list]
  (every? (fn [inp]
            (< (read-string (:last-trade-price (first inp)))
               (read-string (:last-trade-price (second inp)))))
          (take period partitioned-list)))
