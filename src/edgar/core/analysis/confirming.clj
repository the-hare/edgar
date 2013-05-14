(ns edgar.core.analysis.confirming)

(defn on-balance-volume
  "On Balance Volume (OBV) measures buying and selling pressure as a cumulative indicator that i) adds volume on up days and ii) subtracts volume on down days. We'll look for divergences between OBV and price to predict price movements or use OBV to confirm price trends.

   The On Balance Volume (OBV) line is a running total of positive and negative volume. i) A tick's volume is positive when the close is above the prior close. Or ii) a tick's volume is negative when the close is below the prior close.

    If closing price is above prior:
      Current OBV = Previous OBV + Current Volume

    If closing price is below prior:
      Current OBV = Previous OBV  -  Current Volume

    If closing price equals prior:
      Current OBV = Previous OBV (no change)

    ** The first OBV value is the first period's positive/negative volume."
  [latest-tick tick-list]

  )
