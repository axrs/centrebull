(ns centrebull.db.competitions
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [competitions-create!]]))

(def ^:private key-map {:competition/id          :id
                        :competition/description :description
                        :competition/start-date  :start-date
                        :competition/end-date    :end-date})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [competition]
  (->> competition
    in-mapper
    competitions-create!))
