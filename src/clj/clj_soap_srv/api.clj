(ns clj-soap-srv.api
  (:require [clj-soap-srv.interop :as i]))

(defn ping
  [body header]
  (let [{token :echoToken} (bean body)]
    (i/new-ota-ping-rs :token token :messages ["hello"])))

(defn search
  [body header]
  (i/new-ota-search-rs))

(defn read
  [body header]
  (i/new-ota-hotelres-rs))
