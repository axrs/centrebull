(ns centrebull.test.db.activities
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [activity]
    (is (= em activity))
    rv))

(defn delete!
  [ei rv]
  (fn [id]
    (is (= ei id))
    rv))

(defn find-for-competition
  [ei rv]
  (fn [id]
    (is (= ei id))
    rv))
