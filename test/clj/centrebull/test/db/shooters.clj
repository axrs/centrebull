(ns centrebull.test.db.shooters
  (:require [clojure.test :refer :all]))

(defn create!
  "em - Expected shooter map
  rv - Return value"
  [em rv]
  (fn [shooter]
    (is (= shooter em))
    rv))

(defn suggest
  "es - Expected search string
  rv - Return value"
  [es rv]
  (fn [s]
    (is (= s es))
    rv))
