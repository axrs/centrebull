(ns centrebull.competitions.core
  (:refer-clojure :exclude [find get])
  (:require [centrebull.db.competitions :as dao]
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

(defn suggest [{:keys [params]}]
  (response/ok (dao/suggest (:q params))))
