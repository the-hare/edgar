(defproject edgar "0.1.0-SNAPSHOT"
  :description "Edgar is a basic tool for performing technical Analysis on financial instruments"
  :url "http://example.com/FIXME"
  ;;:source-paths ["src"]
  :java-source-paths ["src"]
  :dependencies [
                 ;; Pedestal libraries
                 [org.clojure/clojure "1.5.0"]
                 [io.pedestal/pedestal.service "0.1.6"]
                 [io.pedestal/pedestal.jetty "0.1.6"]

                 ;; Logging
                 [ch.qos.logback/logback-classic "1.0.7"]
                 [org.slf4j/jul-to-slf4j "1.7.2"]
                 [org.slf4j/jcl-over-slf4j "1.7.2"]
                 [org.slf4j/log4j-over-slf4j "1.7.2"]

                 ;; Edgar proper
                 [org.clojure/core.incubator "0.1.2"]
                 [jtsclient/jtsclient "9.8.3"]
                 [org.clojure/data.csv "0.1.2"]
                 [overtone/at-at "1.1.1"]
                 [cljs-uuid "0.0.4"]
                 [org.clojure/tools.namespace "0.2.3"]
                 [lamina "0.5.0-beta15"]
                 [aleph "0.3.0-beta15"]
                 [clj-time "0.5.0"]
                 [com.datomic/datomic "0.8.3335"
                  :exclusions [org.slf4j/slf4j-nop org.slf4j/log4j-over-slf4j]]
                 [midje "1.5.1"]

                 ;; Clojurescript Libraries
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [jayq "2.3.0"]

                 ;; Java Libraries
                 [joda-time "2.2"]
                 ]

  :plugins [[lein-ring "0.8.2"]
            [lein-cljsbuild "0.3.0"]
            [lein-midje "3.0.1"]]
  :cljsbuild {
              :crossovers [edgar.core.analysis]
              :crossover-path "public/templ/cljs"
              :builds [{
                        :incremental true
                        :source-path "public/templ/cljs"
                        :compiler {
                                   :output-to "public/javascript/main.js"
                                   :output-dir "public/javascript/"
                                   :optimizations :simple ;; :whitespace ;; :advanced ;; :simple
                                   :pretty-print true}}]}
  :profiles {:dev {:source-paths ["src", "dev"]
                   :dependencies [[ring-mock "0.1.3"]]}}
  :resource-paths [".:etc/:public/"]
  :repositories { "local" ~(str (.toURI (java.io.File. (str (-> (load-file "etc/config.clj") :dev :root-dir) ".m2/repository/"))))
                  }
  :min-lein-version "2.0.0"
  :main ^{:skip-aot true} edgar.server
  )
