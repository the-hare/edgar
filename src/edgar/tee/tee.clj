(ns edgar.tee.tee
  )


(defprotocol Tee
  "A typed interface for splitting incoming an incoming bucket of stocks. This stock list will have been processed and sorted"

  (tee [bucket] "The processed, incoming list of stocks"))
