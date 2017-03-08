(ns centrebull.db.activities
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [activities-create! activities-delete!]]))

(def ^:private key-map {:activity/id             :id
                        :competition/id :competition-id
                        :activity/range-id       :range-id
                        :activity/priority       :priority
                        :activity/date           :date})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [activity]
  (->> activity
    in-mapper
    activities-create!
    out-mapper))

(defn delete! [id]
  (->> {:id id}
    activities-delete!))
