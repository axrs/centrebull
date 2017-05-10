(ns centrebull.test.aggregates.grand
  (:require
    [clojure.test :refer :all]
    [centrebull.aggregates.grand :as grand]
    [centrebull.db.grand-aggregates :as dao]
    [centrebull.test.db.grand-aggregates :as mock-dao]
    [centrebull.db.aggregates :as aggregates-dao]
    [centrebull.test.db.aggregates :as aggregates-mock-dao]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]))

(deftest grand-aggregates
  (testing "Aggregates-Create!"
    (let [expected (gen-grand-aggregate)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)
                    aggregates-dao/find-for-competition-and-in-coll (aggregates-mock-dao/find-for-competition-and-in-coll expected (:grand-aggregate/aggregates expected))]
        (let [{:keys [status body]} (grand/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200))))))

  (testing "Aggregates-Create! with invalid activity ids"
    (let [expected (gen-grand-aggregate)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)
                    aggregates-dao/find-for-competition-and-in-coll (aggregates-mock-dao/find-for-competition-and-in-coll expected [])]
        (let [{:keys [status body]} (grand/create! {:all-params expected})]
          (is (= body {:errors {:grand-aggregate/aggregates "Not all aggregates found in competition."}}))
          (is (= status 400))))))

  (testing "Aggregates-Find"
    (let [expected {:competition/id (uuid)}
          aggregates [(gen-grand-aggregate)]]
      (with-redefs [dao/find (mock-dao/find expected aggregates)]
        (let [{:keys [status body]} (grand/find-aggregates {:all-params expected})]
          (is (= body aggregates))
          (is (= status 200))))))

  (testing "Aggregates-Find-Results"
    (let [expected {:competition/id (uuid) :activity/id (uuid)}
          aggregates [(gen-aggregate-result)]]
      (with-redefs [dao/find-results (mock-dao/find-results expected aggregates)]
        (let [{:keys [status body]} (grand/find-aggregate-results {:all-params expected})]
          (is (= body aggregates))
          (is (= status 200))))))

  (testing "Aggregates-Delete!"
    (let [all-params {:aggregate/id (uuid) :competition/id (uuid)}]
      (with-redefs [dao/delete-aggregate! (mock-dao/delete-aggregate! all-params nil)]
        (let [{:keys [status body]} (grand/delete-aggregate! {:all-params all-params})]
          (is (= body nil))
          (is (= status 200)))))))

