(ns centrebull.db.entries
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [entries-create]]))



(def ^:private key-map {:competition/id          :id
                        :shooter/id              :sid
                        :shooter/grade           :grade})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [competition]
  (->> competition
    in-mapper
    entries-create))
