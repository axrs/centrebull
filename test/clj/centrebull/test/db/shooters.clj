(ns centrebull.test.db.shooters
  (:require [clojure.test :refer :all]))

(defn create!
  "em - Expected shooter map
  rv - Return value"
  [em rv]
  (fn [shooter]
    (is (= shooter em))
    rv))

(deftest test-prepare-shooter-search-terms
  (testing "prepare-shooter-search-terms"
    (is (= (#'centrebull.db.shooters/prepare-shooter-search-terms "Johnny Search Term")
           ["%johnny%" "%search%" "%term%"]))))