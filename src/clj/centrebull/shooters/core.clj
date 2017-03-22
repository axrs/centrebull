(ns centrebull.shooters.core
  (:require [centrebull.db.shooters :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))

(defn suggest [{:keys [params]}]
  (response/ok (dao/suggest (:search/q params))))

(defn find-by-id [{:keys [params]}]
  (response/ok (dao/find-by-id (:sid params))))
