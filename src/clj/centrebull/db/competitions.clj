(ns centrebull.db.competitions
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper prepare-search-terms]]
            [centrebull.db.core :refer [competitions-create!
                                        competitions-find
                                        competitions-delete!
                                        competitions-suggest
                                        competitions-suggest-registration]]))

(def ^:private key-map {:competition/id          :id
                        :competition/description :description
                        :competition/start-date  :start-date
                        :competition/end-date    :end-date
                        :shooter/last-name       :last-name
                        :shooter/first-name      :first-name
                        :shooter/preferred-name  :preferred-name
                        :shooter/grade           :class
                        :shooter/club            :club
                        :shooter/sid             :sid})


(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [competition]
  (->> competition
       in-mapper
       competitions-create!
       out-mapper))

(defn find [id]
  (->> {:id id}
       competitions-find
       out-mapper))

(defn delete! [id]
  (->> {:id id}
       competitions-delete!))

(defn suggest [s]
  (->> s
       prepare-search-terms
       competitions-suggest
       out-mapper))

(defn suggest-registration [s id]
  (->> {:id id :s (prepare-search-terms s)}
       competitions-suggest-registration
       out-mapper))
