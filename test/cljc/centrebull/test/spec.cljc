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
