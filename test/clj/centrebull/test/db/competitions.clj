(ns centrebull.test.db.competitions
  (:require [clojure.test :refer :all]))

(defn create!
  "em - Expected competition map
  rv - Return value"
  [em rv]
  (fn [competition]
    (is (= em competition))
    rv))
