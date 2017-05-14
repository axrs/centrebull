(ns centrebull.db.entries
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.util :refer [mapper prepare-search-terms]]
            [centrebull.db.core :refer [entries-create!
                                        entries-update-active!
                                        competitions-suggest-registration
                                        competitions-retrieve-all-registrations
                                        competitions-retrieve-registrations
                                        entries-find]]))

(def ^:private key-map {:entry/id               :id
                        :entry/active           :active
                        :competition/id         :competition-id
                        :shooter/sid            :sid
                        :shooter/first-name     :first-name
                        :shooter/last-name      :last-name
                        :shooter/preferred-name :preferred-name
                        :shooter/club           :club
                        :shooter/grade          :class
                        :activity/id            :activity-id
                        :result/vs              :vs
                        :result/score           :score
                        :result/shots           :shots})


(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [entry]
  (->> entry
    in-mapper
    entries-create!
    out-mapper))

(defn find [entry]
  (->> entry
    in-mapper
    entries-find
    out-mapper))

(defn update-active! [id class active?]
  (->> {:id id :class class :active active?}
    entries-update-active!))

(defn suggest-registration [s id]
  (->> {:id id :s (prepare-search-terms s)}
    competitions-suggest-registration
    out-mapper))

(defn retrieve-registrations [comp-id activity-id]
  (->> {:competition-id comp-id :activity-id activity-id}
    competitions-retrieve-registrations
    out-mapper))

(defn retrieve-all-registrations [comp-id]
  (->> {:competition-id comp-id}
    competitions-retrieve-all-registrations
    out-mapper))
