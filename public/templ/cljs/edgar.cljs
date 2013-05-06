(ns edgar
  (:require-macros [shoreleave.remotes.macros :as macros])
  (:require [clojure.browser.repl :as repl]
            [shoreleave.remotes.http-rpc :as rpc]))

;;(repl/connect "http://localhost:9000/repl")


(rpc/remote-callback :heartbeat ["heartbeat"]
                     #(js/alert %))
