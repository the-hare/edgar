(defproject edgar "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojure/core.incubator "0.1.2"]
                 [jtsclient/jtsclient "9.8.3"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler edgar.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :repositories {"local" ~(str (.toURI (java.io.File. "/Users/timothyw/.m2/repository/")))}
  )
