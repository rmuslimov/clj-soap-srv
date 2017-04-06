(ns clj-soap-srv.interop
  (:import [org.opentravel.ota._2003._05 OTAPingRQ OTAPingRS]))

(defn new-ota-ping-rs
  "Generate OTA_PingRS."
  [& {:keys [messages token]}]
  (let [rs (OTAPingRS.)]
    (. rs setEchoToken token)
    (doall
     (for [m messages]
      (-> rs .getSuccessAndWarningsAndEchoData (.add m))))
    rs))
