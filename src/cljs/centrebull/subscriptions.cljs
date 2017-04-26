(ns centrebull.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _] (:page db)))

(reg-sub
  :sidebar-open?
  (fn [db _] (:sidebar-open? db)))

(reg-sub
  :force-sidebar-open?
  (fn [db _] (:force-sidebar-open? db)))

(reg-sub
  :active-competition
  (fn [db _] (:active-competition db)))

(reg-sub
  :active-competition-id
  (fn [db _] (get-in db [:active-competition :competition/id])))

(reg-sub
  :activities
  (fn [db _] (:activities db)))

(reg-sub
  :active-activity
  (fn [db _] (:active-activity db)))
