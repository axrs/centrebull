(ns centrebull.test.shooters.core
  (:require
    [clojure.test :refer :all]
    [centrebull.shooters.core :as shooters]
    [centrebull.db.shooters :as dao]
    [centrebull.test.db.shooters :as mock-dao]))

(defn- gen-shooter []
  {:sid            "1234"
   :first-name     "Lachlan"
   :last-name      "Robertson"
   :preferred-name nil
   :club           "IT@JCU"})

(deftest shooters
  (testing "Shooter-Create!"
    (let [expected (gen-shooter)]
      (with-redefs [dao/create! (mock-dao/create! expected nil)]
        (let [{:keys [status body]} (shooters/create! {:body-params expected})]
          (is (= body nil))
          (is (= status 200))))))

  (testing "shooter-suggest"
    (let [rv (gen-shooter) es "Johnny Search Term"]
      (with-redefs [dao/suggest (mock-dao/suggest es rv)]
        (let [{:keys [status body]} (shooters/suggest {:query-params {"q" es}})]
          (is (= body (gen-shooter)))
          (is (= status 200)))))))

(deftest prepare-shooter-search-terms
  (testing "prepare-shooter-search-terms"
    (is (= (#'centrebull.db.shooters/prepare-shooter-search-terms "Johnny Search Term")
           ["%Johnny%" "%Search%" "%Term%"]))))