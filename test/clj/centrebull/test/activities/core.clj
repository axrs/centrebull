(ns centrebull.test.activities.core
  (:require
    [clojure.test :refer :all]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.activities.core :as activities]
    [centrebull.db.activities :as dao]
    [centrebull.test.db.activities :as mock-dao]))

(deftest activities
  (testing "Activity-Create!"
    (let [expected (gen-activity)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (activities/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200)))))))
