(ns centrebull.test.aggregates.core
  (:require
    [clojure.test :refer :all]
    [centrebull.aggregates.core :as aggregates]
    [centrebull.db.aggregates :as dao]
    [centrebull.db.activities :as activities-dao]
    [centrebull.test.db.aggregates :as mock-dao]
    [centrebull.test.db.activities :as activities-mock-dao]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]))

(deftest aggregates
  (testing "Aggregates-Create!"
    (let [expected (gen-aggregate)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)
                    activities-dao/find-for-competition-and-in-coll (activities-mock-dao/find-for-competition-and-in-coll expected (:aggregate/activities expected))]
        (let [{:keys [status body]} (aggregates/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200))))))

  (testing "Aggregates-Create! with invalid activity ids"
    (let [expected (gen-aggregate)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)
                    activities-dao/find-for-competition-and-in-coll (activities-mock-dao/find-for-competition-and-in-coll expected [])]
        (let [{:keys [status body]} (aggregates/create! {:all-params expected})]
          (is (= body {:errors {:aggregate/activities "Not all activies found in competition."}}))
          (is (= status 400))))))

  (testing "Aggregates-Find"
    (let [expected {:competition/id (uuid)}
          aggregates [(gen-aggregate)]]
      (with-redefs [dao/find-aggregates (mock-dao/find-aggregates expected aggregates)]
        (let [{:keys [status body]} (aggregates/find-aggregates {:all-params expected})]
          (is (= body aggregates))
          (is (= status 200))))))

  (testing "Aggregates-Find-Results"
    (let [expected {:competition/id (uuid) :activity/id (uuid)}
          aggregates [(gen-aggregate-result)]]
      (with-redefs [dao/find-aggregates (mock-dao/find-results expected aggregates)]
        (let [{:keys [status body]} (aggregates/find-aggregate-results {:all-params expected})]
          (is (= body aggregates))
          (is (= status 200))))))

  (testing "Aggregates-Delete!"
    (let [all-params {:aggregate/id (uuid) :competition/id (uuid)}]
      (with-redefs [dao/delete-aggregate! (mock-dao/delete-aggregate! all-params nil)]
        (let [{:keys [status body]} (aggregates/delete-aggregate! {:all-params all-params})]
          (is (= body nil))
          (is (= status 200)))))))
            
