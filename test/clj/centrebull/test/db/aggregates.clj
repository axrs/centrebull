(ns centrebull.test.db.aggregates
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [aggregate]
    (is (= em aggregate))
    rv))

(defn find-aggregates
  [em rv]
  (fn [competition-id]
    (is (= em competition-id))
    rv))

(defn delete-aggregate!
  [em rv]
  (fn [id-map]
    (is (= em id-map))
    rv))

(defn find-results
  [em rv]
  (fn [id-map]
    (is (= em id-map))
    rv))

(defn find-for-competition-and-in-coll
  [em rv]
  (fn [aggregates competition-id]
    (is (= aggregates (:grand-aggregate/aggregates em)))
    (is (= competition-id (:competition/id em)))
    rv))
