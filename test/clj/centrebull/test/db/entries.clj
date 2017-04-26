(ns centrebull.test.db.entries
  (:require [clojure.test :refer :all]))

(defn find [m rv]
  (fn [all-params]
    (is (= all-params m))
    rv))

(defn update-active! [expected-id expected-active]
  (fn [id active?]
    (is (= id expected-id))
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
  [expected-id rv]
  (fn [id]
    (is (= expected-id id))
    rv))

(defn do-not-call
  [_]
  (throw (Exception. "Should not have been called")))
