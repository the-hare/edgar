(ns edgar.core.signal.common)


(defn find-peaks-valleys [options tick-list]

  (let [{input-key :input
         :or {input-key :last-trade-price}} options]

    (reduce (fn [rslt ech]
              (let [fst (read-string (input-key (first ech)))
                    snd (read-string (input-key (second ech)))
                    thd (read-string (input-key (nth ech 2)))
                    valley? (and (> fst snd) (< snd thd))
                    peak? (and (< fst snd) (> snd thd))]

                (if (or valley? peak?)
                  (if peak?
                    (conj rslt (assoc (second ech) :signal :peak))
                    (conj rslt (assoc (second ech) :signal :valley)))
                  rslt)))
            []
            (partition 3 1 tick-list))))


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
