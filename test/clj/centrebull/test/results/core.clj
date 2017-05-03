(ns centrebull.test.results.core
  (:require [clojure.test :refer :all]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.test.util :refer [do-not-call]]
            [centrebull.db.results :as dao]
            [centrebull.test.db.results :as mock-dao]
            [centrebull.results.core :as results]))

(deftest results
  (testing "Results-Create!"
    (let [expected (gen-result)]
      (with-redefs [dao/create! (mock-dao/create! expected expected)
                    dao/exists? (constantly false)]
        (let [{:keys [status body]} (results/create! {:all-params expected})]
          (is (= body expected))
          (is (= status 200))))))

  (testing "Results-Create! conflict"
    (let [result (gen-result)]
      (with-redefs [dao/create! do-not-call
                    dao/exists? (constantly true)]
        (let [{:keys [status body]} (results/create! {:all-params result})]
          (is (= body {:errors {:conflict "Results for shooter on this activity already exist!"}}))
          (is (= status 409)))))))
