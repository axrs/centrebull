(ns centrebull.db.entries
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.util :refer [mapper prepare-search-terms]]
            [centrebull.db.core :refer [entries-create!
                                        entries-update-active!
                                        competitions-suggest-registration
                                        competitions-retrieve-registrations
                                        entries-find]]))

(def ^:private key-map {:entry/id               :id
                        :competition/id         :competition-id
                        :shooter/sid            :sid
                        :shooter/first-name     :first-name
                        :shooter/last-name      :last-name
                        :shooter/preferred-name :preferred-name
                        :shooter/club           :club
                        :shooter/grade          :class})

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

(defn update-active! [id active?]
  (->> {:id id :active active?}
    entries-update-active!))

(defn suggest-registration [s id]
  (->> {:id id :s (prepare-search-terms s)}
    competitions-suggest-registration
    out-mapper))

(defn retrieve-registrations [id]
  (->> {:competition-id id}
    competitions-retrieve-registrations
    out-mapper))
