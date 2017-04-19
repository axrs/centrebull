(ns centrebull.db.entries
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [entries-create! entries-withdraw!]]))

(def ^:private key-map {:entry/id       :id
                        :competition/id :competition-id
                        :shooter/sid     :sid
                        :shooter/grade  :class})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [entry]
  (->> entry
       in-mapper
       entries-create!
       out-mapper))

(defn withdraw! [id]
  (->> {:id id}
       entries-withdraw!))
