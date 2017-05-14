(ns centrebull.test.db.entries
  (:require [clojure.test :refer :all]))

(defn find [m rv]
  (fn [all-params]
    (is (= all-params m))
    rv))

(defn update-active! [expected-id expected-grade expected-active]
  (fn [id grade active?]
    (is (= id expected-id))
    (if active? (= grade expected-grade)
                (= grade "not active"))
    (is (= active? expected-active))))

(defn create! [m]
  (fn [all-params]
    (is (= all-params m))
    m))

(defn suggest-registration
  [expected-q expected-id rv]
  (fn [q id]
    (is (= q expected-q))
    (is (= id expected-id))
    rv))

(defn retrieve-registrations
  [expected-comp-id expected-activity-id rv]
  (fn [comp-id activity-id]
    (is (= expected-comp-id comp-id))
    (is (= expected-activity-id activity-id))
    rv))

(defn retrieve-all-registrations
  [expected-comp-id rv]
  (fn [comp-id]
    (is (= expected-comp-id comp-id))
    rv))
