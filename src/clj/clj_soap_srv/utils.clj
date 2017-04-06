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
  [obj & {:keys [result?] :or {:result? false}}]
  (let [pname (-> obj meta :param)
        info (get meta-mapping pname)
        objname (str obj)
        result (if result?
                 `{WebResult ~(assoc info :name pname) WebMethod {:action ~objname}}
                 `{WebParam ~(assoc info :name pname)})]
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
           `(~(get-obj-meta mname :result? true)
             [this#
              ~@(for [arg args]
                  (get-obj-meta arg))]
             ~body))))

(comment
  (macroexpand-1
   '(defconcur ConcurHotel
      (^{:param "OTA_PingRS"}
       ping [^{:param "OTA_PingRQ"} body
             ^{:param "authentication"} header]
       (api/ping body header))))

  (set! *print-meta* true)
  )
