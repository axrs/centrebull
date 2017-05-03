(ns centrebull.db.results
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [results-create!]]))

(def ^:private key-map {:result/id    :id
                        :result/vs    :vs
                        :result/score :score
                        :result/shots :shots
                        :shooter/sid  :sid
                        :activity/id  :activity-id})

(def default-fields {:vs 0
                     :score 0})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [result]
  (->> result
       in-mapper
       (merge default-fields)
       results-create!
       out-mapper))
