(ns clj-soap-srv.interop
  (:require [clojure.java.data :refer [to-java]])
  (:import [org.opentravel.ota._2003._05 HotelReservationType HotelResResponseType OTAHotelSearchRS OTAPingRS UniqueIDType]))

(defn new-ota-ping-rs
  [& {:keys [messages token]}]
  (to-java OTAPingRS {:echoToken token :successAndWarningsAndEchoData messages}))


;; Testing converation for simple item
(defmethod to-java
  [OTAPingRS clojure.lang.APersistentMap]
  [cls {:keys [echoToken errors version primaryLangID successAndWarningsAndEchoData]}]
  (let [rs (doto (OTAPingRS.)
             (. setEchoToken echoToken)
             (. setErrors errors)
             (. setVersion version)
             (. setPrimaryLangID primaryLangID))]
    (doall
     (for [m successAndWarningsAndEchoData]
       (-> rs .getSuccessAndWarningsAndEchoData (.add m))))
    rs))

;; (bean
;;  (to-java
;;   OTAPingRS {:echoToken "23" :successAndWarningsAndEchoData ["1@"]}))

(defn new-ota-search-rs
  []
  (OTAHotelSearchRS.))

(defn new-ota-hotelres-rs
  []
  (HotelResResponseType.))
