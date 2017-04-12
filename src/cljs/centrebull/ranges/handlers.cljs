(ns centrebull.ranges.handlers
  (:require [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-fx
  :ranges-create
  (fn [_ [_ state after-success]]
      (post-json {:url           "/ranges"
                  :body          state
                  :after-success after-success})))
