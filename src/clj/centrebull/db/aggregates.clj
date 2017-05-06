(ns centrebull.db.aggregates
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [aggregates-create! aggregates-find]]))

(def ^:private key-map {:aggregate/activities  :activities
                        :aggregate/description :description
                        :aggregate/priority    :priority
                        :competition/id        :competition-id})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [aggregate]
  (->> aggregate
       in-mapper
       aggregates-create!
       out-mapper))

(defn find-aggregates [competition-id]
  (->> competition-id
       in-mapper
       aggregates-find
       out-mapper))

(defn delete-aggregate! [_])