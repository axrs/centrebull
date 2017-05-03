(ns centrebull.results.core
  (:require [centrebull.db.results :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))
