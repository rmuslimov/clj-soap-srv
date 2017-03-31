(ns user
  (:require [com.stuartsierra.component :as component]
            [clj-soap-srv.core :refer [enabled-endpoints]]
            [clj-soap-srv.utils :refer [new-jaxrs-server]]
            [clojure.tools.namespace.repl :refer [refresh]]
            reloaded.repl))

(defn dev-system []
  (component/system-map
   :jax-rs (new-jaxrs-server enabled-endpoints)))

(defn reload-system!
  "Useful when in`lein repl`."
  []
  (reloaded.repl/suspend)
  (refresh)
  (reloaded.repl/resume))

(reloaded.repl/set-init! #'dev-system)
