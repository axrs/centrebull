(ns centrebull.competitions.handlers
  (:require [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-fx
  :competitions-create
  (fn [_ [_ state errors after-success]]
    (post-json {:url           "/competitions"
                :body          state
                :errors        errors
                :after-success after-success})))

(reg-event-db
  :set-active-competition
  (fn [db [_ competition]]
    (assoc db :active-competition competition)))
