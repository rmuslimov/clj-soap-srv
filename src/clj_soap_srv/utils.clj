(ns clj-soap-srv.utils
  (:require [com.stuartsierra.component :as component]))

;; Component system declaration for JaxWs
;; Here is short intro to idea: https://github.com/stuartsierra/component

(defrecord JaxWsServer [endpoints]
  component/Lifecycle
  (start [{:keys [endpoints] :as this}]
    (let [started
          (into
           {}
           (for [[k {:keys [create-fn] :as v}] endpoints]
             [k (create-fn)]))]
      (assoc this :started started)))

  (stop [{:keys [started] :as this}]
    (dorun
     (for [[k item] started]
       (.stop item)))))

(defn new-jaxws-server [endpoints]
  (JaxWsServer. endpoints))
