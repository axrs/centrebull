(ns centrebull.aggregates.handlers
  (:require
    [centrebull.ajax :refer [post-json get-json delete-json]]
    [goog.string :as gstring]
    [goog.string.format]
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

(def sort-format (partial gstring/format "%04d"))

(defn- find-max-priorities [results]
  (->> results (map :aggregate/priority) distinct set last))

(defn- calc-sort-str [max results]
  (loop [p max
         return []]
    (if (not= -1 p)
      (let [r (first (filter #(= p (:aggregate/priority %)) results))]
        (recur (dec p) (concat return [(sort-format (or (:result/score r) 0)) (sort-format (or (:result/vs r) 0))])))
      return)))

(defn- translate-grade [g]
  (cond
    (= "A" g) "10"
    (= "B" g) "09"
    (= "C" g) "08"
    (= "D" g) "07"
    (= "FS" g) "06"
    (= "FS1" g) "05"
    (= "FS2" g) "04"
    (= "FO" g) "03"
    (= "FO1" g) "02"
    (= "FO2" g) "01"
    :else "00"))

(defn- update-shooter [max-priorities]
  (fn [[sid v]]
    (let [r (first v)
          g (:shooter/grade r)
          results (->> v (mapv #(select-keys % [:result/score :result/vs :aggregate/priority])))
          score (->> v (map :result/score) (apply +))
          vs (->> v (map :result/vs) (apply +))
          sort-key (calc-sort-str max-priorities results)]
      (-> r
        (dissoc :activity/id :aggregate/description :result/score :result/vs)
        (assoc :aggregate/score score :aggregate/vs vs :aggregate/results results)
        (assoc :sort-key (str (translate-grade g)
                           (sort-format (or score 0))
                           (sort-format (or vs 0))
                           (apply str sort-key)))))))

(reg-event-fx
  ::set-active-aggregate-results
  (fn [{:keys [db]} [_ results]]
    (let [h (find-max-priorities results)
          pred (update-shooter h)
          grouped (group-by :shooter/sid results)]

      {:db (assoc db :active-aggregate-results (->> grouped
                                                 (map pred)
                                                 (sort-by :sort-key >)))})))

(reg-event-fx
  :refresh-aggregate-results
  (fn [{:keys [db]} _]
    (let [competition-id (get-in db [:active-competition :competition/id])
          aggregate-id (get-in db [:active-aggregate :aggregate/id])]
      (if competition-id
        (get-json {:url           (str "competitions/" competition-id "/aggregates/" aggregate-id "/results")
                   :after-success [[::set-active-aggregate-results]]})
        {}))))

(reg-event-fx
  :set-active-aggregate
  (fn [{:keys [db]} [_ id]]
    {:db         (assoc db :active-aggregate (first (filter #(= id (:aggregate/id %)) (:aggregates db))))
     :dispatch-n [[:set-active-page :aggregate] [:refresh-aggregate-results]]}))
