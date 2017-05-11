(ns centrebull.grand-aggregates.handlers
  (:require
    [centrebull.ajax :refer [post-json get-json delete-json]]
    [re-frame.core :refer [dispatch reg-event-fx]]))

(reg-event-fx
  ::set-active-grand-aggregates
  (fn [{:keys [db]} [_ results]]
    {:db (assoc db :grand-aggregates results)}))
    
(reg-event-fx
  :grand-aggregate-create
  (fn [{:keys [db]} [_ state & after-success]]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (post-json {:url           (str "competitions/" competition-id "/grand-aggregates")
                  :body          state
                  :after-success after-success}))))

(reg-event-fx
  :refresh-grand-aggregates
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/grand-aggregates")
                   :after-success [[::set-active-grand-aggregates]]})
        {}))))

(reg-event-fx
  :grand-aggregates-load
  (fn [_ _]
    {:dispatch-n [[:set-active-page :grand-aggregates]
                  ; [:refresh-activities]
                  [:refresh-grand-aggregates]]}))