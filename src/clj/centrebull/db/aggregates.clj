(ns centrebull.db.aggregates
  (:require [clojure.string :refer [split lower-case]]
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
            [centrebull.db.core :refer [aggregates-create!
                                        aggregates-find
                                        aggregates-find-results
                                        aggregates-delete!]]))

(def ^:private key-map {:aggregate/id          :id
                        :aggregate/activities  :activities
                        :aggregate/description :description
                        :aggregate/priority    :priority
                        :competition/id        :competition-id
                        :shooter/grade         :class
                        :shooter/sid           :sid
                        :shooter/first-name    :first-name
                        :shooter/last-name     :last-name
                        :shooter/club          :club
                        :result/score          :score
                        :result/vs             :vs
                        :activity/id           :activity-id
                        :range/description     :description})

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

(defn delete-aggregate! [delete-params]
  (->> delete-params
    in-mapper
    aggregates-delete!))

(defn find-results [find-params]
  (->> find-params
    in-mapper
    aggregates-find-results
    out-mapper))
