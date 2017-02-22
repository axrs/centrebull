(ns centrebull.shooters.core
  (:require [centrebull.db.shooters :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [all-params]}]
  (response/ok (dao/create! all-params)))

(defn suggest [{:keys [params]}]
  (response/ok (dao/suggest (:q params))))

(defn findById [{:keys [params]}]
  (response/ok (dao/findById (:q params))))
