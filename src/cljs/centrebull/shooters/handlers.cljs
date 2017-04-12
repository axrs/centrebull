(ns centrebull.shooters.handlers
  (:require [centrebull.ajax :refer [post-json delete-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-fx
  :shooters-create
  (fn [_ [_ state errors after-success]]
    (post-json {:url           "/shooters"
                :body          state
                :errors        errors
                :after-success after-success})))

(reg-event-fx
  :shooters-register
  (fn [_ [_ body results errors after-success]]
    (post-json {:url "/registrations"
                :body body
                :errors errors
                :after-success after-success})))

(reg-event-fx
  :update-registered-shooters
  (fn [_ [_ body results]]
    (prn results)
    (let [sid (:shooter/sid body)]
      (swap! results #(map (fn [shooter] (if (= (:shooter/sid shooter) sid)
                                           (assoc shooter :competition/id (:competition/id body))
                                           shooter))
                           @results)))))

(reg-event-fx
  ::shooters-unregister-in-atom
  (fn [_ [_ id results]]
    (prn results)
    (swap! results #(map (fn [shooter] (if (= (:entry/id shooter) id)
                                         (dissoc shooter :entry/id)
                                         shooter))
                         @results))))

(reg-event-fx
  :shooters-unregister
  (fn [_ [_ id results]]
    (prn "dicks")
    (delete-json {:url (str "/registrations/" id)
                  :after-success [[::shooters-unregister-in-atom id results]]})))
