(ns centrebull.activities.handlers
  (:require [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-fx]]))


(reg-event-fx
  :activities-load
  (fn [_ _]
    {:dispatch-n [[:set-active-page :activities]
                  [:refresh-activities]]}))


(reg-event-fx
  :refresh-activities
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (if competition-id
        (post-json {:url           "/activities"
                    :body          state
                    :errors        errors
                    :after-success after-success}))
      {})))


(reg-event-fx
  :activities-create
  (fn [_ [_ state errors after-success]]
    (post-json {:url           "/activities"
                :body          state
                :errors        errors
                :after-success after-success})))
