(ns centrebull.results.core
  (:require [centrebull.db.results :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (if (dao/exists? all-params)
      (response/conflict {:errors {:conflict "Results for shooter on this activity already exist!"}})
      (response/ok (dao/create! all-params))))

