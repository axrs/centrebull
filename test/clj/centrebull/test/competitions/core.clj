(ns centrebull.test.competitions.core
  (:require
    [clojure.test :refer :all]
    [centrebull.test.util :refer [uuid]]
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
          (is (= status 200))))))

  (testing "Competition-Find"
    (let [id (uuid)
          expected (gen-competition id)]
      (with-redefs [dao/find (mock-dao/find id expected)]
        (let [{:keys [status body]} (competitions/find {:all-params expected})]
          (is (= body expected))
          (is (= status 200))))))

  (testing "Competition-Delete!"
    (let [id (uuid)
          expected (gen-competition id)]
      (with-redefs [dao/delete! (mock-dao/delete! id expected)]
        (let [{:keys [status body]} (competitions/delete! {:all-params expected})]
          (is (= body expected))
          (is (= status 200))))))

  (testing "Competition-Suggest"
    (let [expected (gen-competition)
          es "Johnny Search Term"]
      (with-redefs [dao/suggest (mock-dao/suggest es expected)]
        (let [{:keys [status body]} (competitions/suggest {:params {:q es}})]
          (is (= body expected))
          (is (= status 200)))))))
