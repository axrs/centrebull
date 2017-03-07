(ns centrebull.test.db.activities
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [activity]
    (is (= em activity))
    rv))
