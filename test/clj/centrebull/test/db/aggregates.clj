(ns centrebull.test.db.aggregates
  (:require [clojure.test :refer :all]))
(defn create!
  [em rv]
  (fn [aggregate]
    (is (= em aggregate))
    rv))  
