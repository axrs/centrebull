(ns centrebull.aggregates.core
  (:require [centrebull.db.aggregates :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))

(defn find-aggregates [{:keys [all-params]}]
  (response/ok (dao/find-aggregates all-params)))

(defn delete-aggregate! [{:keys [all-params]}]
  (response/ok (dao/delete-aggregate! all-params)))
