(ns centrebull.test.db.ranges
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [range]
    (is (= em range))
    rv))

(defn delete!
  [ei rv]
  (fn [id]
    (is (= ei id))
    rv))
