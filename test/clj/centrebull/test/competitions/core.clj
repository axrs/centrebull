(ns centrebull.test.competitions.core
  (:require
    [clojure.test :refer :all]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.competitions.core :as competitions]
    [centrebull.db.competitions :as dao]
    [centrebull.test.db.competitions :as mock-dao]))

(deftest competitions
  (testing "Competition-Create!"
    (let [expected (gen-competition)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (competitions/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200)))))))
