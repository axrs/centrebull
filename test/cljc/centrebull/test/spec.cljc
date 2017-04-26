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

(defn- random-uuid []
  #?(:clj  (UUID/randomUUID)
     :cljs (UUID/make-random)))

(def ^:private ->uuid #'centrebull.spec/->uuid)
(deftest test->uuid
  (testing "Converts the first parameter to a UUID object, or returns ::s/invalid"
    (is (uuid? (->uuid "2c80c3ca-535c-4706-bea2-afd2a2bf374d")))
    (is (uuid? (->uuid (random-uuid))))
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
    (is-not (shot-length-10-15 "VVV"))
    (is-not (shot-length-10-15 ""))
    (is-not (shot-length-10-15 "VVV444555"))
    ;;10
    (is (shot-length-10-15 "VVV4445556"))
    (is (shot-length-10-15 "VVV44455566"))
    (is (shot-length-10-15 "VVV444555666"))
    (is-not (shot-length-10-15 "VVV4445556667"))
    (is-not (shot-length-10-15 "VVV44455566677"))
    ;;15
    (is (shot-length-10-15 "VVV444555666777"))
    (is (shot-length-10-15 "VVV4445556667778"))
    (is (shot-length-10-15 "VVV44455566677788"))
    (is-not (shot-length-10-15 "VVV444555666777788"))))

(def ^:private valid-shot-chars-only #'centrebull.spec/valid-shot-chars-only)
(deftest test-valid-shot-chars-only
  (testing "Should ensure only valid chracters are used"
    (is (true? (valid-shot-chars-only "VVV")))
    (is (true? (valid-shot-chars-only "-0123456VXXX")))
    (is (false? (valid-shot-chars-only "a")))
    (is (false? (valid-shot-chars-only "VVV4VVV5VVa")))))

(def ^:private calculate-vs #'centrebull.spec/calculate-vs)
(deftest test-calculate-vs
  (testing "Should count the number of Vs in a string"
    (is (= 0 (calculate-vs "")))
    (is (= 3 (calculate-vs "VVV")))
    (is (= 3 (calculate-vs "aV VxV")))))

(def ^:private calculate-score #'centrebull.spec/calculate-score)
(deftest test-calculate-score
  (testing "Should count the score values"
    (is (= 0 (calculate-score "")))
    (is (= 16 (calculate-score "VV42")))
    (is (= 19 (calculate-score "VV54")))
    (is (= 30 (calculate-score "XXX")))
    (is (= 50 (calculate-score "34VVVVVVVVVV")))
    (is (= 74 (calculate-score "34VVVVVVVVVVVVVV4")))
    (is (= 75 (calculate-score "34VVVVVVVVVVVVVVV")))))

(def ^:private calculate-result #'centrebull.spec/calculate-result)
(deftest test-calculate-result
  (testing "Should calculate :score and :vs from a map containing :shots"
    (is (= {:result/score 0 :result/vs 0} (calculate-result {})))
    (is (= {:result/score 0 :result/vs 0 :result/shots ""} (calculate-result {:result/shots ""})))
    (is (= {:result/score 10 :result/vs 2 :result/shots "VV"} (calculate-result {:result/shots "VV"})))
    (is (= {:result/score 9 :result/vs 1 :result/shots "V4"} (calculate-result {:result/shots "V4"})))))

(deftest test-shots-spec
  (testing "Testing the spec for result/shots"
    (is (s/valid? :result/shots "VVVVVVVVVV"))
    (is-not (s/valid? :result/shots ""))
    (is-not (s/valid? :result/shots "abcde"))
    (is-not (s/valid? :result/shots "VVVVVV"))))

(deftest test-activity-result-spec
  (testing "Should calculate results if all required keys exist"
    (is-not (s/valid? :api/activity-result {}))
    (is-not (s/valid? :api/activity-result {:result/shots ""}))
    (is-not (s/valid? :api/activity-result {:result/shots ""
                                            :activity/id  (random-uuid)}))
    (is-not (s/valid? :api/activity-result {:result/shots ""
                                            :shooter/sid  123
                                            :activity/id  (random-uuid)}))
    (is-not (s/valid? :api/activity-result {:result/shots "VVV"
                                            :shooter/sid  123
                                            :activity/id  (random-uuid)}))
    (is-not (s/valid? :api/activity-result {:result/shots "1234567890123456"
                                            :shooter/sid  123
                                            :activity/id  (random-uuid)}))
    (is-not (s/valid? :api/activity-result {:result/shots "a234567890123456"
                                            :shooter/sid  123
                                            :activity/id  (random-uuid)}))
    (is (s/valid? :api/activity-result {:result/shots "1234512345"
                                        :shooter/sid  123
                                        :activity/id  (random-uuid)}))
    (is (s/valid? :api/activity-result {:result/shots "123451234512345"
                                        :shooter/sid  123
                                        :activity/id  (random-uuid)}))
    (let [u (random-uuid)]
      (is (= {:result/shots "123451234512345"
              :shooter/sid  123
              :activity/id  u
              :result/score 45
              :result/vs    0}
            (s/conform :api/activity-result {:result/shots "123451234512345"
                                             :shooter/sid  123
                                             :activity/id  u}))))))
