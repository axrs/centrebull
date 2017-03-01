(ns centrebull.db.ranges
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [ranges-create!]]))

(def ^:private key-map {:range/description :description})

(defn- in-mapper [m] (mapper key-map m))

(defn create! [range]
  (->> range
    in-mapper
    ranges-create!))
