(ns centrebull.shooters.handlers
  (:require [centrebull.ajax :refer [post-json delete-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-fx
  :shooters-create
  (fn [_ [_ state & after-success]]
    (post-json {:url           "/shooters"
                :body          state
                :after-success after-success})))

(reg-event-fx
  :shooters-register
  (fn [_ [_ body & after-success]]
    (post-json {:url           "/registrations"
                :body          body
                :after-success after-success})))

(reg-event-fx
  :update-registered-shooters
  (fn [_ [_ body results result]]
    (let [sid (:shooter/sid body)]
      (swap! results #(map (fn [shooter] (if (= (:shooter/sid shooter) sid)
                                           (-> shooter
                                             (assoc :competition/id (:competition/id result))
                                             (assoc :entry/id (:entry/id result)))
                                           shooter))
                        @results)))
    {}))

(reg-event-fx
  ::shooters-unregister-in-atom
  (fn [_ [_ id results]]
    (swap! results #(map (fn [shooter] (if (= (:entry/id shooter) id)
                                         (-> shooter
                                           (dissoc :entry/id)
                                           (dissoc :competition/id))
                                         shooter))
                      @results))))

(reg-event-fx
  :shooters-unregister
  (fn [_ [_ id results shooter]]
    (delete-json {:url           (str "/registrations/" id)
                  :after-success [[::shooters-unregister-in-atom id results shooter]]})))
