(ns centrebull.test.results.core
  (:require [clojure.test :refer :all]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.db.results :as dao]
            [centrebull.test.db.results :as mock-dao]
            [centrebull.results.core :as results]))

(deftest results
  (testing "Results-Create!"
    (let [expected (gen-result)]
      (with-redefs [dao/create! (mock-dao/create! expected expected)]
        (let [{:keys [status body]} (results/create! {:all-params expected})]
          (is (= body expected))
          (is (= status 200)))))))
