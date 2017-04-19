(ns centrebull.registrations.core
  (:refer-clojure :exclude [find get])
  (:require [centrebull.db.entries :as dao]
    [ring.util.http-response :as response]
    [clojure.tools.logging :as log]))

(defn register-shooter! [{:keys [all-params]}]
  (let [existing-entries (dao/find all-params)
        id (:entry/id existing-entries)]
    (if (empty? existing-entries)
        (->> all-params
             dao/create!
             response/ok)
        (do (dao/update-active! id true)
            (->> all-params
                 dao/find
                 response/ok)))))


(defn unregister-shooter! [{:keys [all-params]}]
  (response/ok (dao/update-active! (:entry/id all-params) false)))

(defn suggest-registration [{:keys [all-params] :as req}]
  (response/ok (dao/suggest-registration (:search/q all-params) (:competition/id all-params))))
