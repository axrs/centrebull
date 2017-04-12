(ns centrebull.db.ranges
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper prepare-search-terms]]
            [centrebull.db.core :refer [ranges-create! ranges-delete! ranges-suggest]]))

(def ^:private key-map {:range/description :description
                        :range/id          :id})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [range]
  (->> range
       in-mapper
       ranges-create!
       out-mapper))

(defn delete! [id]
  (->> {:id id}
       ranges-delete!))


(defn suggest [s]
  (->> s
       prepare-search-terms
       ranges-suggest
       out-mapper))
