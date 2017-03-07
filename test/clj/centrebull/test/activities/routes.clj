(ns centrebull.test.activities.routes
  (:require [clojure.test :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]
            [centrebull.activities.core :as activities]
            [centrebull.test.util :refer :all]
            [ring.util.http-response :as response]))

(use-fixtures :once wrap-test)

(deftest test-activities-route
  (testing "Create Activity Route - Invalid spec"
    (with-redefs [activities/create! (constantly (response/ok {:activity-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/activities" nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))

  (testing "Create Activity Route"
    (with-redefs [activities/create! (constantly (response/ok {:activity-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/activities" nil))]
        (is (= status 200))
        (is (= {:competition-create! "called"} (parse-body body)))))))
