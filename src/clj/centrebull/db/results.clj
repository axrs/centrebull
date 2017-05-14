(ns centrebull.db.results
  (:require [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [results-create! results-exist? results-update!]]))

(def ^:private key-map {:result/id           :id
                        :result/vs           :vs
                        :result/score        :score
                        :result/shots        :shots
                        :result/shots-mirror :shots-mirror
                        :shooter/sid         :sid
                        :activity/id         :activity-id})

(def default-fields {:vs    0
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

(defn update! [result]
  (->> result
    in-mapper
    results-update!
    prn
    out-mapper))

(defn exists? [result]
  (->> result
    in-mapper
    results-exist?
    :exists))
