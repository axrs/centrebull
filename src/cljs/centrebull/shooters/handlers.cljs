(ns centrebull.shooters.handlers
  (:require [centrebull.ajax :refer [post-json]]
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
    (post-json {:url (str "/competitions/" (:competition/id body) "/registrations")
                :body body
                :errors errors
                :after-success after-success})))

(reg-event-fx
  :update-registed-shooters
  (fn [_ [_ body results]]
    (let [sid (:shooter/sid body)]
      (swap! results #(map (fn [shooter] (if (= (:shooter/sid shooter) sid)
                                           (assoc shooter :competition/id (:competition/id body))
                                           shooter))
                           @results)))))
