(ns centrebull.test.db.competitions
  (:require [clojure.test :refer :all]))

(defn create!
  "em - Expected competition map
  rv - Return value"
  [em rv]
  (fn [competition]
    (is (= em competition))
    rv))

(defn find
  "ei - Expected competition UUID
  rv - Return Value"
  [ei rv]
  (fn [id]
    (is (= ei id))
    rv))

(defn delete!
  [ei rv]
  (fn [id]
    (is (= ei id))
    rv))

(defn suggest
  [es rv]
  (fn [s]
    (is (= s es))
    rv))

(defn suggest-registration
  [expected-q expected-id rv]
  (fn [q id]
    (is (= q expected-q))
    (is (= id expected-id))
    rv))
