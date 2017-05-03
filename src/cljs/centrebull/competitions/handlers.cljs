(ns centrebull.competitions.handlers
  (:require [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-fx
  :competitions-create
  (fn [_ [_ state & after-success]]
    (post-json {:url           "/competitions"
                :body          state
                :after-success after-success})))

(reg-event-fx
  :set-active-competition
  (fn [{:keys [db]} [_ competition]]
    {:db         (assoc db :active-competition competition)
     :dispatch-n [[:set-page-url "/shooters"] [:refresh-activities]]}))
