(ns dev
  (:require [com.stuartsierra.component :as component]
            [clj-soap-srv.core :refer [enabled-endpoints]]
            [clj-soap-srv.utils :refer [new-jaxws-server]]
            [clojure.tools.namespace.repl :refer [refresh]]
            reloaded.repl))

(defn reload-system!
  "Useful when in`lein repl`."
  []
  (reloaded.repl/suspend)
  (refresh)
  (reloaded.repl/resume))

(defn dev-system []
  (component/system-map
   :jax-ws (new-jaxws-server enabled-endpoints)))

(reloaded.repl/set-init! #'dev-system)
