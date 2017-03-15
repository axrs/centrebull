(ns centrebull.handlers
  (:require [centrebull.db :as db]
            [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :toggle-sidebar
  (fn [db [_]]
    (assoc db :sidebar-open? (not (:sidebar-open? db)))))

(reg-event-db
  :try-force-sidebar-open
  (fn [db [_ value]]
    (assoc db :force-sidebar-open? (> value 800))))

(reg-event-fx
  :set-page-width
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc db :page-width value)
     :dispatch [:try-force-sidebar-open value]}))

(reg-event-fx ::update-results-atom
  (fn [_ [_ ratom results]]
    (reset! ratom results)))

(reg-event-fx
  :search
  (fn [_ [_ url params ratom]]
    (post-json {:url           url
                :body          params
                :after-success [[::update-results-atom ratom]]})))
