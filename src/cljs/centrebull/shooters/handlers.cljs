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
