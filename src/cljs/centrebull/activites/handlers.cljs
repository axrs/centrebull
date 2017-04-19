(ns centrebull.activities.handlers
  (:require [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-fx]]))

(reg-event-fx
  :activities-create
  (fn [_ [_ state errors after-success]]
    (post-json {:url           "/activities"
                :body          state
                :errors        errors
                :after-success after-success})))
