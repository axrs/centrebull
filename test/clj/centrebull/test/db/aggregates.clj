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
