(ns centrebull.competitions.core
  (:require [centrebull.db.competitions :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))
