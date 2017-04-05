(ns clj-soap-srv.core
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [clj-soap-srv.api :as api]
            [clj-soap-srv.utils :refer [new-jaxws-server]])
  (:import [javax.jws WebService WebParam WebResult WebMethod]
           javax.xml.ws.Endpoint
           [org.opentravel.ota._2003._05 OTAPingRQ OTAPingRS]
           [ota2015b ConcurHotelServiceEndpoint]
           javax.jws.soap.SOAPBinding
           ))

(def hotel-info-url "http://localhost:8080/HotelInfoWS")
(definterface IHotelInfoWS
  (^String GetHotelName [^String hotelId]))

(deftype ^{WebService {:targetNamespace "http://tripsource.com/"}}
    HotelInfoWS []
    IHotelInfoWS
    (GetHotelName [this
                   ^{WebParam {:name "hotelId"}} hotel_id]
      (api/get-hotel-name hotel_id)))

(def concur-url "http://localhost:8080/ConcurHotelServiceWS")
(deftype ^{WebService {:name "ConcurHotelServiceEndpoint"
                       :targetNamespace "http://www.opentravel.org/OTA/2003/05"}}
    ConcurHotel []
  ConcurHotelServiceEndpoint
  (
   ^{WebMethod {:action "ping"}
     WebResult {:name "OTA_PingRS"
                :targetnamespace "http://www.opentravel.org/OTA/2003/05"
                :partName "Body"}}
   ping [this
         ^{WebParam {:name "OTA_PingRQ"
                     :targetNamespace "http://www.opentravel.org/OTA/2003/05"
                     :partName "Body"}} body
         ^{WebParam {:name "authentication"
                     :targetNamespace "http://www.concur.com/webservice/auth"
                     :header true
                     :partName "Header"}} header]
   (let [token (. body getEchoToken)]
    (doto (OTAPingRS.)
     (-> .getSuccessAndWarningsAndEchoData (.add "hello"))
     (. setEchoToken token)))))

(def enabled-endpoints
  {:hotel-info
   {:create-fn (fn [] (Endpoint/publish hotel-info-url (HotelInfoWS.)))
    :endpoint nil}
   :concur-hotel-service
   {:create-fn (fn [] (Endpoint/publish concur-url (ConcurHotel.)))
    :endpoint nil}
   })

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
