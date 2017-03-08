(ns centrebull.test.db.entries
  (:require [clojure.test :refer :all]))

(defn create!
  "em - Expected register data map
  rv - Return value"
  [em rv]
  (fn [register]
    (is (= em register))
    rv))
