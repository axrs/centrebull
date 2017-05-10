(ns centrebull.db.grand-aggregates
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [grand-aggregates-create!
                                        aggregate-find-results
                                        grand-aggregates-find
                                        grand-aggregates-find-by-id
                                        grand-aggregates-delete!]]))

(def ^:private key-map {:grand-aggregate/id         :id
                        :grand-aggregate/aggregates :aggregates
                        :aggregate/description      :description
                        :aggregate/priority         :priority
                        :competition/id             :competition-id
                        :shooter/grade              :class
                        :shooter/sid                :sid
                        :shooter/first-name         :first-name
                        :shooter/last-name          :last-name
                        :shooter/club               :club
                        :result/score               :score
                        :result/vs                  :vs
                        :activity/id                :activity-id
                        :range/description          :description})

(def ^:private value-map (map-invert key-map))

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [aggregate]
  (->> aggregate
    in-mapper
    grand-aggregates-create!
    out-mapper))

(defn find [competition-id]
  (->> competition-id
    in-mapper
    grand-aggregates-find
    out-mapper))

(defn find-by-id [find-params]
  (->> find-params
    in-mapper
    grand-aggregates-find-by-id
    out-mapper))

(defn delete-aggregate! [delete-params]
  (->> delete-params
    in-mapper
    grand-aggregates-delete!))

(defn- find-grand-results [aggregates]
  (loop [agg aggregates
         results []]
    (if (not-empty agg)
      (recur (next aggregates) (concat results (aggregate-find-results (assoc find-params :aggregate/id (first agg)))))
      results)))

(defn find-results [find-params]
  (->> find-params
    in-mapper
    find-by-id
    :aggregates
    find-grand-results
    out-mapper))
