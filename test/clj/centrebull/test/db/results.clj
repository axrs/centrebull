(ns centrebull.test.db.results
  (:require [clojure.test :refer :all]))

(defn create!
  [em rv]
  (fn [result]
    (is (= em result))
    rv))
