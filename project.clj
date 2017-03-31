(defproject clj-soap-srv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [com.stuartsierra/component "0.3.2"]
                 [javax.ws.rs/javax.ws.rs-api "2.0"]
                 [reloaded.repl "0.2.3"]]
  :main ^:skip-aot clj-soap-srv.core
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["dev"]}
             :uberjar {:aot :all}})
