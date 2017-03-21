(ns centrebull.activities.core
  (:require [centrebull.db.activities :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))

(defn delete! [{:keys [all-params]}]
  (->> all-params
       :activity/id
       dao/delete!
       response/ok))
