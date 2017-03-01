(ns centrebull.test.ranges.core
  (:require
    [clojure.test :refer :all]
    [centrebull.ranges.core :as ranges]
    [centrebull.db.ranges :as dao]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.test.db.ranges :as mock-dao]))

(deftest ranges
  (testing "ranges-create!"
    (let [expected (gen-range)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (ranges/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200)))))))

