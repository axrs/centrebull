(ns centrebull.aggregates.core
  (:require [centrebull.db.aggregates :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))
