(ns centrebull.activities.handlers
  (:require [centrebull.ajax :refer [post-json get-json]]
            [re-frame.core :refer [dispatch reg-event-fx]]
            [re-frame.core :as rf]
            [centrebull.utils :refer [rank-results]]))

(reg-event-fx
  :activity-create
  (fn [_ [_ state & after-success]]
    (post-json {:url           "/activities"
                :body          state
                :after-success after-success})))

(reg-event-fx
  :activity-create-result
  (fn [_ [_ state & after-success]]
    (post-json {:url           "/results"
                :body          state
                :after-success after-success})))

(reg-event-fx
  ::set-active-activites
  (fn [{:keys [db]} [_ results]]
    {:db (assoc db :activities (rank-results results))}))

(reg-event-fx
  :refresh-activities
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/activities")
                   :after-success [[::set-active-activites]]})
        {}))))

(reg-event-fx
  :activities-create
  (fn [_ [_ state errors after-success]]
    (post-json {:url           "/activities"
                :body          state
                :errors        errors
                :after-success after-success})))

(reg-event-fx
  :activities-load
  (fn [_ _]
    {:dispatch-n [[:set-active-page :activities]
                  [:refresh-activities]]}))

(reg-event-fx
  :set-active-activity
  (fn [{:keys [db]} [_ id]]
    {:db         (assoc db :active-activity (first (filter #(= id (:activity/id %)) (:activities db))))
     :dispatch-n [[:set-active-page :activity] [:refresh-activity-results]]}))

(reg-event-fx
  :refresh-activity-results
  (fn [{:keys [db]} _]
    (let [activity (get-in db [:active-activity])
          a-id (:activity/id activity)
          c-id (:competition/id activity)]
      (if c-id
        (post-json {:url           (str "competitions/" c-id "/registrations")
                    :body          activity
                    :after-success [[::set-active-activity-results a-id]]})
        {}))))

(reg-event-fx
  ::set-active-activity-results
  (fn [{:keys [db]} [_ id results]]
    (let [a-id (get-in db [:active-activity :activity/id])]
      (if (= a-id id)
        {:db (assoc db :active-activity-results (rank-results results))}
        {}))))

(reg-event-fx
  :refresh-all-results
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/registrations/all")
                   :after-success [[::set-all-results]]})
        {}))))

(reg-event-fx
  ::set-all-results
  (fn [{:keys [db]} [_ results]]
    {:db (assoc db :all-results results)}))
