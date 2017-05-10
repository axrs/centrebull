(ns centrebull.aggregates.grand
  (:require
    [centrebull.db.grand-aggregates :as dao]
    [centrebull.db.aggregates :as aggregates-dao]
    [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (let [aggregates (:grand-aggregate/aggregates all-params)
        competition-id (:competition/id all-params)
        aggregate-count (count aggregates)
        valid-aggregate-count (count (aggregates-dao/find-for-competition-and-in-coll aggregates competition-id))]
    (if (= aggregate-count valid-aggregate-count)
      (response/ok (dao/create! all-params))
      (response/bad-request {:errors {:grand-aggregate/aggregates "Not all aggregates found in competition."}}))))

(defn find-aggregates [{:keys [all-params]}]
  (response/ok (dao/find all-params)))

(defn delete-aggregate! [{:keys [all-params]}]
  (response/ok (dao/delete-aggregate! all-params)))

(defn find-aggregate-results [{:keys [all-params]}]
  (response/ok (dao/find-results all-params)))
