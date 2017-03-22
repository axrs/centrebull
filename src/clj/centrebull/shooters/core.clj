(ns centrebull.shooters.core
  (:require [centrebull.db.shooters :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))

(defn suggest [{:keys [all-params]}]
  (response/ok (dao/suggest (:search/q all-params))))

(defn find-by-id [{:keys [all-params]}]
  (response/ok (dao/find-by-id (:sid all-params))))
