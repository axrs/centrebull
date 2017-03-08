(ns centrebull.db.shooters
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [shooters-create! shooters-suggest shooters-find-by-id]]))

(def ^:private key-map {:shooter/sid            :sid
                        :shooter/first-name     :first-name
                        :shooter/last-name      :last-name
                        :shooter/preferred-name :preferred-name
                        :shooter/club           :club})

(def ^:private value-map (map-invert key-map))

(def default-fields {:club           ""
                     :preferred-name ""})

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [shooter]
  (->> shooter
    in-mapper
    (merge default-fields)
    shooters-create!))

(defn suggest [s]
  (->> s
    prepare-search-terms
    shooters-suggest
    out-mapper))

(defn find-by-id [sid]
  (->> {:sid sid}
       shooters-find-by-id
       out-mapper))
