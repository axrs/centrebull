(ns centrebull.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :sidebar-open?
  (fn [db _]
    (:sidebar-open? db)))

(reg-sub
  :force-sidebar-open?
  (fn [db _]
    (:force-sidebar-open? db)))
