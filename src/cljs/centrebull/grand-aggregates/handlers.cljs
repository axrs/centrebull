(ns centrebull.grand-aggregates.handlers
  (:require
    [centrebull.ajax :refer [post-json get-json delete-json]]
    [re-frame.core :refer [dispatch reg-event-fx]]
    [centrebull.utils :refer [rank-results sorted-results]]))

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
                  [:refresh-grand-aggregates]]}))

(reg-event-fx
  ::set-active-grand-aggregate-results
  (fn [{:keys [db]} [_ results]]
   {:db (assoc db :active-grand-aggregate-results (rank-results (sorted-results results)))}))

(reg-event-fx
  :refresh-grand-aggregate-results
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])
          grand-aggregate-id (get-in db [:active-grand-aggregate :grand-aggregate/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/grand-aggregates/" grand-aggregate-id "/results")
                   :after-success [[::set-active-grand-aggregate-results]]})
        {}))))

(reg-event-fx
  :set-active-grand-aggregate
  (fn [{:keys [db]} [_ id]]
    {:db         (assoc db :active-grand-aggregate (first (filter #(= id (:grand-aggregate/id %)) (:grand-aggregates db))))
     :dispatch-n [[:set-active-page :grand-aggregate-page] [:refresh-grand-aggregate-results]]}))

(reg-event-fx
  :grand-aggregates-delete
  (fn [{:keys [db]} [_ id & after-success]]
    (let [competition-id (get-in db [:active-competition :competition/id])]
      (delete-json {:url           (str "competitions/" competition-id "/grand-aggregates/" id)
                    :after-success after-success}))))
