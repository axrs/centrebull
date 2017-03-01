(ns centrebull.test.db.util
  (:require [clojure.test :refer :all]
            [centrebull.db.util :refer [mapper]]
            [clojure.set :refer [map-invert]]))

(def ^:private key-map {:company/name :name
                        :company/id   :id})

(def ^:private value-map (map-invert key-map))

(deftest db-in-mapper-test
  (testing "Testing database collection remapping for database insertion"
    (let [expected {:name "AXRS" :id "1234"}
          input {:company/name "AXRS" :company/id "1234" :invalid/field "ignored"}
          actual (mapper key-map input)]
      (is (= actual expected)))))

(deftest db-out-mapper-test
  (testing "Testing database collection remapping for database retrieval"
    (let [expected {:company/name "AXRS" :company/id "1234"}
          input {:name "AXRS" :id "1234" :field "ignored"}
          actual (mapper value-map input)]
      (is (= actual expected)))))
