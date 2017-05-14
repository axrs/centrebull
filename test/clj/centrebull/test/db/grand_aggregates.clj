(ns centrebull.test.db.grand-aggregates
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [aggregate]
    (is (= em aggregate))
    rv))

(defn find
  [em rv]
  (fn [competition-id]
    (is (= em competition-id))
    rv))

(defn find-by-id
  [em rv]
  (fn [param-map]
    (is (= em param-map))
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

(defn find-tv-results
  [em rv]
  (fn [id-map]
    (is (= em id-map))
    rv))
