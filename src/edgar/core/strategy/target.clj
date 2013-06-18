(ns edgar.core.strategy.target)


(defn percentage-change [orig-price current-price]

  (let [change-prc (- current-price orig-price)
        change-pct (/ change-prc orig-price)]

    change-pct))

(defn stoploss-threshhold? [orig-price current-price]

  (let [change-pct (percentage-change orig-price current-price)
        change-test (> -0.025 change-pct)]

    change-test))


(defn stoploss-threshhold-wstocks? [orig-price current-price no-shares]

  (let [balance-org (* orig-price no-shares)
        balance-crt (* current-price no-shares)
        balance-trg (+ balance-org (* balance-org -0.025))
        change-test (< balance-crt balance-trg)]

    (println (str "... > test[" change-test "] > current[" balance-crt "] > threshold[" balance-trg "]"))
    change-test))
