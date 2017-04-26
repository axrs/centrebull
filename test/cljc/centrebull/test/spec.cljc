(ns centrebull.test.spec
  #?(:clj
     (:import java.util.UUID))
  (:require
    [clojure.test :refer [deftest testing is]]
    [centrebull.test.extend :refer [is-not]]
    [clojure.spec :as s]
    #?(:cljs [cljs-uuid-utils.core :as UUID])))

(def ^:private uuid-str? #'centrebull.spec/uuid-str?)

(deftest test-uuid-str?
  (testing "uuid-str? is a UUID string"
    (is (uuid-str? "2c80c3ca-535c-4706-bea2-afd2a2bf374d"))
    (is (not (uuid-str? "1234")))
    (is (not (uuid-str? 1234)))
    (is (not (uuid-str? nil)))))

(def ^:private ->uuid #'centrebull.spec/->uuid)
(deftest test->uuid
  (testing "Converts the first parameter to a UUID object, or returns ::s/invalid"
    (is (uuid? (->uuid "2c80c3ca-535c-4706-bea2-afd2a2bf374d")))
    #?(:clj  (is (uuid? (->uuid (UUID/randomUUID))))
       :cljs (is (uuid? (->uuid (UUID/make-random)))))
    (is (s/invalid? (->uuid "1234")))
    (is (s/invalid? (->uuid 1234)))
    (is (s/invalid? (->uuid nil)))))

(s/def ::non-empty-string centrebull.spec/non-empty-string)
(deftest test-non-empty-string
  (testing "Testing spec predicate for non-empty strings"
    (is (s/valid? ::non-empty-string "The whole idea of creating saints, it's pure 'Monty Python.' They have to clock up two miracles."))
    (is-not (s/valid? ::non-empty-string ""))
    (is-not (s/valid? ::non-empty-string nil))
    (is-not (s/valid? ::non-empty-string 123))))

(def ^:private shot-length-10-15 #'centrebull.spec/shot-length-10-15)
(deftest test-score-length
  (testing "Conforms an incoming :activity/shots is of length 10-12 or 15-17 inclusive"
    (is (s/invalid? (shot-length-10-15 "VVV")))
    (is (s/invalid? (shot-length-10-15 "")))
    (is (s/invalid? (shot-length-10-15 "VVV444555")))
    ;;10
    (is (= "VVV4445556" (shot-length-10-15 "VVV4445556")))
    (is (= "VVV44455566" (shot-length-10-15 "VVV44455566")))
    (is (= "VVV444555666" (shot-length-10-15 "VVV444555666")))
    (is (s/invalid? (shot-length-10-15 "VVV4445556667")))
    (is (s/invalid? (shot-length-10-15 "VVV44455566677")))
    ;;15
    (is (= "VVV444555666777" (shot-length-10-15 "VVV444555666777")))
    (is (= "VVV4445556667778" (shot-length-10-15 "VVV4445556667778")))
    (is (= "VVV44455566677788" (shot-length-10-15 "VVV44455566677788")))
    (is (s/invalid? (shot-length-10-15 "VVV444555666777788")))))

(def ^:private valid-shot-chars-only #'centrebull.spec/valid-shot-chars-only)
(deftest test-valid-shot-chars-only
  (testing "Should ensure only valid chracters are used"
    (is (= "VVV" (valid-shot-chars-only "VVV")))
    (is (= "-0123456VXXX" (valid-shot-chars-only "-0123456VXXX")))
    (is (s/invalid? (valid-shot-chars-only "a")))
    (is (s/invalid? (valid-shot-chars-only "VVV4VVV5VVa")))))
