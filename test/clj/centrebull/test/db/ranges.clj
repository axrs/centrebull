(ns centrebull.test.db.ranges
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [range]
    (is (= range em))
    rv))
