(ns clj-soap-srv.api)

(defn get-hotel-name
  "Return hotel name by id."
  [hotel_id]
  (format "Hotel name: %s" hotel_id))
