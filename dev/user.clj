(ns user
  (:require [com.stuartsierra.component :as component]
            [clj-soap-srv.core :refer [enabled-endpoints]]
            [clj-soap-srv.utils :refer [new-jaxrs-server]]
            reloaded.repl))

(defn dev-system []
  (component/system-map
   :jax-rs (new-jaxrs-server enabled-endpoints)))

(reloaded.repl/set-init! #'dev-system)
