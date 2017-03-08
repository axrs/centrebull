(ns centrebull.test.shooters.core
  (:require
    [clojure.test :refer :all]
    [centrebull.shooters.core :as shooters]
    [centrebull.db.shooters :as dao]
    [centrebull.test.db.shooters :as mock-dao]))

(defn- gen-shooter []
  {:shooter/sid            "1234"
   :shooter/first-name     "Lachlan"
   :shooter/last-name      "Robertson"
   :shooter/preferred-name nil
   :shooter/club           "IT@JCU"})

(deftest shooters
  (testing "Shooter-Create!"
    (let [expected (gen-shooter)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (shooters/create! {:all-params expected})]
          (is (= body nil))
          (is (= status 200))))))

  (testing "shooter-suggest"
    (let [expected (gen-shooter)
          es "Johnny Search Term"]
      (with-redefs [dao/suggest (mock-dao/suggest es expected)]
        (let [{:keys [status body]} (shooters/suggest {:params {:q es}})]
          (is (= body expected))
          (is (= status 200)))))))

(testing "shooter-find-by-id"
  (let [expected (gen-shooter)
        es "1234"]
    (with-redefs [dao/find-by-id (mock-dao/find-by-id es expected)]
      (let [{:keys [status body]} (shooters/find-by-id {:params {:sid es}})]
        (is (= body expected))
        (is (= status 200))))))

(def ^:private prepare-terms #'centrebull.db.shooters/prepare-shooter-search-terms)

(deftest prepare-shooter-search-terms
  (testing "prepare-shooter-search-terms"
    (let [actual (prepare-terms "Johnny Search Term")]
      (is (= actual ["%Johnny%" "%Search%" "%Term%"])))))
