(ns centrebull.test.ranges.core
  (:require
    [clojure.test :refer :all]
    [centrebull.ranges.core :as ranges]
    [centrebull.db.ranges :as dao]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.test.db.ranges :as mock-dao]))

(deftest ranges
  (testing "ranges-create!"
    (let [expected (gen-range)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (ranges/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200))))))
  (testing "ranges-delete!"
    (let [id (uuid)
          expected (gen-range id)]
      (with-redefs [dao/delete! (mock-dao/delete! id expected)]
        (let [{:keys [status body]} (ranges/delete! {:all-params expected})]
          (is (= body expected))
          (is (= status 200)))))))
