(ns centrebull.shooters.core
  (:require [centrebull.db.shooters :as dao]
            [ring.util.http-response :as response]))

(defn create! [{:keys [body-params]}]
  (response/ok (dao/create! body-params)))

(defn suggest [{:keys [query-params]}]
  (response/ok (dao/suggest (query-params "q"))))