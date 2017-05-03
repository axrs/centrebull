(ns centrebull.test.aggregates.core
  (:require
    [clojure.test :refer :all]
    [centrebull.aggregates.core :as aggregates]
    [centrebull.db.aggregates :as dao]
    [centrebull.test.db.aggregates :as mock-dao]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]))

(deftest aggregates
  (testing "Aggregates-Create!"
    (let [expected (gen-aggregate)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (aggregates/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200)))))))

