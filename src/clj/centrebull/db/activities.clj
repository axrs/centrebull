(ns centrebull.db.activities
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [activities-create!
                                        activities-delete!
                                        activities-find-for-competition
                                        activities-find-for-competition-and-in-coll]]))

(def ^:private key-map {:activity/id       :id
                        :competition/id    :competition-id
                        :range/description :description
                        :activity/range-id :range-id
                        :activity/priority :priority
                        :activity/date     :date})

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

(defn find-for-competition [id]
  (->> {:competition-id id}
    activities-find-for-competition
    out-mapper))

(defn find-for-competition-and-in-coll [activities competition-id]
  (->> {:activities activities :competition-id competition-id}
    activities-find-for-competition-and-in-coll))
    
