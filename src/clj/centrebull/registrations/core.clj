(ns centrebull.registrations.core
  (:refer-clojure :exclude [find get])
  (:require [centrebull.db.entries :as dao]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))

(defn register-shooter! [{:keys [all-params]}]
  (let [id (-> all-params
             dao/find
             :entry/id)
        class (:shooter/grade all-params)]
    (if (nil? id)
      (->> all-params
        dao/create!
        response/ok)
      (do (dao/update-active! id class true)
          (->> all-params
            dao/find
            response/ok)))))


(defn unregister-shooter! [{:keys [all-params]}]
  (response/ok (dao/update-active! (:entry/id all-params) "not active" false)))

(defn suggest-registration [{:keys [all-params] :as req}]
  (response/ok (dao/suggest-registration (:search/q all-params) (:competition/id all-params))))

(defn retrieve-registrations [{:keys [all-params]}]
  (response/ok (dao/retrieve-registrations (:competition/id all-params) (:activity/id all-params))))
