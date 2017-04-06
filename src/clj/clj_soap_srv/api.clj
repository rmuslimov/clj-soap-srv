(ns clj-soap-srv.api
  (:require [clj-soap-srv.interop :refer [new-ota-ping-rs]]))

(defn ping
  [body header]
  (let [{token :echoToken} (bean body)]
    (new-ota-ping-rs :token token :messages ["hello"])))
