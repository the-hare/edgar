(ns edgar.core.signal.common)


(defn find-peaks-valleys [options tick-list]

  (let [{input-key :input
         :or {input-key :last-trade-price}} options]

    (reduce (fn [rslt ech]
              (let [fst (if (string? (input-key (first ech)))
                          (read-string (input-key (first ech)))
                          (input-key (first ech)))

                    snd (if (string? (input-key (second ech)))
                          (read-string (input-key (second ech)))
                          (input-key (second ech)))

                    thd (if (string? (input-key (nth ech 2)))
                          (read-string (input-key (nth ech 2)))
                          (input-key (nth ech 2)))

                    valley? (and (and (-> fst nil? not) (-> snd nil? not) (-> thd nil? not))
                                 (> fst snd)
                                 (< snd thd))
                    peak? (and (and (-> fst nil? not) (-> snd nil? not) (-> thd nil? not))
                               (< fst snd)
                               (> snd thd))]

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
