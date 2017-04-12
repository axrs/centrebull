(ns centrebull.test.competitions.core
  (:require
    [clojure.test :refer :all]
    [centrebull.test.util :refer [uuid]]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.competitions.core :as competitions]
    [centrebull.db.competitions :as dao]
    [centrebull.db.entries :as dao-entries]
    [centrebull.test.db.competitions :as mock-dao]
    [centrebull.test.db.entries :as mock-dao-entries]))

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

  (testing "Competition-Register!"
    (let [expected (gen-competition-regester-request)]
      (with-redefs [dao-entries/create! (mock-dao-entries/create! expected nil)]
        (let [{:keys [status body]} (competitions/register-shooter! {:all-params expected})]
          (is (= body nil))
          (is (= status 200))))))

  (testing "Competition-Suggest"
    (let [expected (gen-competition)
          es "Johnny Search Term"]
      (with-redefs [dao/suggest (mock-dao/suggest es expected)]
        (let [{:keys [status body]} (competitions/suggest {:all-params {:search/q es}})]
          (is (= body expected))
          (is (= status 200))))))

  (testing "Competition-suggest-registration"
    (let [expected (gen-shooters-registered)
          expected-input (gen-search-query)
          id (:competition/id expected-input)
          q (:search/q expected-input)]
      (with-redefs [dao/suggest-registration (mock-dao/suggest-registration q id expected)]
        (let [{:keys [status body]} (competitions/suggest-registration {:all-params expected-input})]
          (is (= body expected))
          (is (= status 200)))))))
