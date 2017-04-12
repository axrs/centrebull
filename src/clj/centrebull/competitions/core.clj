(ns centrebull.competitions.core
  (:refer-clojure :exclude [find get])
  (:require [centrebull.db.competitions :as dao]
            [centrebull.db.entries :as dao-entries]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))

(defn create! [{:keys [all-params]}]
  (log/debug "Creating a new competition" all-params)
  (response/ok (dao/create! all-params)))

(defn find [{:keys [all-params]}]
  (->> all-params
       :competition/id
       dao/find
       response/ok))

(defn delete! [{:keys [all-params]}]
  (->> all-params
       :competition/id
       dao/delete!
       response/ok))

(defn register-shooter! [{:keys [all-params]}]
  (->> all-params
       dao-entries/create!
       response/ok))

(defn unregister-shooter! [{:keys [all-params]}]
  (->> all-params
       :entry/id
       dao-entries/delete!
       response/ok))

(defn suggest [{:keys [all-params]}]
  (response/ok (dao/suggest (:search/q all-params))))

(defn suggest-registration [{:keys [all-params] :as req}]
  (clojure.pprint/pprint req)
  (response/ok (dao/suggest-registration (:search/q all-params) (:competition/id all-params))))
