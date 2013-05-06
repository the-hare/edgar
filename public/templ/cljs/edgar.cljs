(ns edgar
  #_(:require-macros [shoreleave.remotes.macros :as macros])
  #_(:require [clojure.browser.repl :as repl]
            [shoreleave.remotes.http-rpc :as rpc]))

;;(repl/connect "http://localhost:9000/repl")


#_(rpc/remote-callback :handler/heartbeat ["heartbeat"]
                     #(js/alert %))
