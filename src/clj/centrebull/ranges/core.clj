(ns centrebull.ranges.core
  (:require [centrebull.db.ranges :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))
