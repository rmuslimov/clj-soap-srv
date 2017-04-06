(ns clj-soap-srv.core
  (:gen-class)
  (:require [clj-soap-srv
             [api :as api]
             [utils :refer [defconcur new-jaxws-server]]]
            [com.stuartsierra.component :as component])
  (:import javax.xml.ws.Endpoint
              [javax.jws WebService WebParam WebResult WebMethod]
))

(def concur-endpoint-url "http://localhost:8080/ConcurHotelServiceWS")
(defconcur ConcurHotel
  (^{:param "OTA_PingRS"}
   ping [^{:param "OTA_PingRQ"} body
         ^{:param "authentication"} header]
   (api/ping body header)))

(def enabled-endpoints
  {:concur-hotel-service
   {:create-fn (fn [] (Endpoint/publish concur-endpoint-url (ConcurHotel.)))
    :endpoint nil}})

(defn prod-system
  "Production systems to start."
  []
  (component/system-map
   :jax-ws (new-jaxws-server enabled-endpoints)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (component/start (prod-system))
  (println "System has been started..."))
