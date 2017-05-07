(ns centrebull.aggregates.core
  (:require [centrebull.db.aggregates :as dao]
            [ring.util.http-response :as response]
            [centrebull.db.activities :as activities-dao]))

(defn create! [{:keys [all-params]}]
  (let [activities (:aggregate/activities all-params)
        competition-id (:competition/id all-params)
        activities-count (count activities)
        valid-activities-count (count (activities-dao/find-for-competition-and-in-coll activities competition-id))]
    (if (= activities-count valid-activities-count)                                                                   
      (response/ok (dao/create! all-params))
      (response/bad-request {:errors {:aggregate/activities "Not all activies found in competition."}}))))

(defn find-aggregates [{:keys [all-params]}]
  (response/ok (dao/find-aggregates all-params)))

(defn delete-aggregate! [{:keys [all-params]}]
  (response/ok (dao/delete-aggregate! all-params)))
