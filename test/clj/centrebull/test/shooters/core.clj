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
          (is (= status 200)))))))
