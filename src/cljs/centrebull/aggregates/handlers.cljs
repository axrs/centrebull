(ns centrebull.aggregates.handlers
  (:require [centrebull.ajax :refer [post-json get-json delete-json]]
            [re-frame.core :refer [dispatch reg-event-fx]]
            [re-frame.core :as rf]))

(reg-event-fx
  ::set-active-aggregates
  (fn [{:keys [db]} [_ results]]
    {:db (assoc db :aggregates results)}))

(reg-event-fx
  :refresh-aggregates
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/aggregates")
                   :after-success [[::set-active-aggregates]]})
        {}))))

(reg-event-fx
  :aggregates-load
  (fn [_ _]
    {:dispatch-n [[:set-active-page :aggregates]
                  [:refresh-activities]
                  [:refresh-aggregates]]}))

(reg-event-fx
  :aggregate-create
  (fn [{:keys [db]} [_ state & after-success]]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (post-json {:url           (str "competitions/" competition-id "/aggregates")
                  :body          state
                  :after-success after-success}))))

(reg-event-fx
  :activities-delete
  (fn [{:keys [db]} [_ id & after-success]]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (delete-json {:url           (str "competitions/" competition-id "/aggregates/" id)
                    :after-success after-success}))))
