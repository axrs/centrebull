(ns centrebull.shooters.core
  (:require [centrebull.db.shooters :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [body-params]}]
  (response/ok (dao/create! body-params)))

(defn suggest [{:keys [params]}]
  (response/ok (dao/suggest (:q params))))

(defn find-by-id [{:keys [params]}]
  (response/ok (dao/find-by-id (:id params))))
