(ns edgar
  (:use [jayq.core :only [$ css inner]])
  (:require-macros [shoreleave.remotes.macros :as macros])
  (:require [clojure.browser.repl :as repl]
            [shoreleave.remotes.http-rpc :as rpc]))

#_(repl/connect "http://localhost:9000/repl")

(rpc/remote-callback :heartbeat ["heartbeat"]
                     #()
                     #_#(js/alert %))

;; Adding vertical tabs. Equivalent to:
;; $( "#tabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
(.addClass (.tabs
            ($ "#tabs"))
           "ui-tabs-vertical ui-helper-clearfix")

;; Equivalent to: $( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
(.addClass (.removeClass ($ "#tabs li")
                         "ui-corner-top")
           "ui-corner-left")
