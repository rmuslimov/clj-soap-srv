(ns clj-soap-srv.utils
  (:gen-class)
  (:require [com.stuartsierra.component :as component])
  (:import
   [javax.jws WebService WebParam WebResult WebMethod]
   [ota2015b ConcurHotelServiceEndpoint]))

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

;;;   Macros around concur jax-ws service    ---------------------

(def ota "http://www.opentravel.org/OTA/2003/05")
(def cnc "http://www.concur.com/webservice/auth")

(def meta-mapping
  {"OTA_PingRQ" {:targetNamespace ota :partName "Body"}
   "OTA_PingRS" {:targetNamespace ota :partName "Body"}
   "authentication" {:targetNamespace cnc :partName "Header" :header true}})

(defn get-obj-meta
  "Get information for ws, based on param name."
  [obj]
  (let [metaobj (meta obj)
        result? (:result metaobj)
        pname (or (:result metaobj) (:param metaobj))
        info (assoc (get meta-mapping pname) :name pname)
        result (if result?
                 `{WebResult ~info WebMethod {:action ~(str obj)}}
                 `{WebParam ~info})]
    (with-meta obj result)))

(defn ws-service [name]
  (with-meta name
    `{WebService {:name "ConcurHotelServiceEndpoint"
                  :targetNamespace ~ota}}))

(defmacro defconcur
  "Generate deftype for jax-ws api."
  [name & methods]
  `(deftype
       ~(ws-service name) [] ConcurHotelServiceEndpoint
       ~@(for [[mname args body] methods]
           `(~(get-obj-meta mname)
             [this#
              ~@(for [arg args]
                  (get-obj-meta arg))]
             ~body))))

(comment
  (macroexpand-1
   '(defconcur ConcurHotel
      (^{:result "OTA_PingRS"}
       ping [^{:param "OTA_PingRQ"} body
             ^{:param "authentication"} header]
       (api/ping body header))))

  (set! *print-meta* true)
  )
