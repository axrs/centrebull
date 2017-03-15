(ns cljc.centrebull.util
  (:require
    [clojure.test :refer [is deftest testing]]))

(defmacro is-not
  ([form] `(is (not ~form)))
  ([form msg] `(is (not ~form ~msg))))

(deftest test-is-not
  (testing "Testing is-not evaluator"
    (is-not false)
    (is-not (not true))))
