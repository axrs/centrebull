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

(reg-sub :activities (fn [db _] (:activities db)))

(reg-sub :aggregates (fn [db _] (:aggregates db)))

(reg-sub :grand-aggregates (fn [db _] (:grand-aggregates db)))

(reg-sub :aggregates-and-activities
  (fn [db _]
    (->> (:aggregates db)
      (into (:activities db))
      (into (:grand-aggregates db))
      (mapv #(assoc % :priority (or (:aggregate/priority %) (:activity/priority %))))
      (sort-by :priority))))

(reg-sub :active-activity (fn [db _] (:active-activity db)))
(reg-sub :active-activity-results (fn [db _] (:active-activity-results db)))
(reg-sub :active-aggregate (fn [db _] (:active-aggregate db)))
(reg-sub :active-aggregate-results (fn [db _] (:active-aggregate-results db)))

(reg-sub :active-grand-aggregate (fn [db _] (:active-grand-aggregate db)))
(reg-sub :active-grand-aggregate-results (fn [db _] (:active-grand-aggregate-results db)))
