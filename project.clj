(defproject edgar "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src"]
  :java-source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojure/core.incubator "0.1.2"]
                 [jtsclient/jtsclient "9.8.3"]
                 [org.clojure/data.csv "0.1.2"]
                 [overtone/at-at "1.1.1"]
                 [cljs-uuid "0.0.4"]
                 [org.clojure/tools.namespace "0.2.3"]
                 [lamina "0.5.0-beta15"]
                 [clj-time "0.5.0"]
                 [com.datomic/datomic "0.8.3335"
                  :exclusions [org.slf4j/slf4j-nop org.slf4j/log4j-over-slf4j]]
                 [org.slf4j/slf4j-log4j12 "1.6.4"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.2.0-SNAPSHOT"]
                 [ibdknox/clojurescript "0.0-1534"]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [com.cemerick/shoreleave-remote-ring "0.0.2"]

                 ;; Java Libraries
                 [joda-time "2.2"]
                 ]
  :plugins [[lein-ring "0.8.2"]
            [lein-cljsbuild "0.3.0"]]
  :cljsbuild {
              :builds [{
                        :source-path "public/templ/cljs"
                        :compiler {
                                   :output-to "public/javascript/main.js"
                                   :output-dir "public/javascript/"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :ring {:handler edgar.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :resource-paths ["etc/:public/"]
  :repositories { "local" ~(str (.toURI (java.io.File. (str (-> (load-file "etc/config.clj") :dev :root-dir) ".m2/repository/"))))
                  }
  :main edgar.handler
  )
