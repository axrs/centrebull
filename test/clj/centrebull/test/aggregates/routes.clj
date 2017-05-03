(ns centrebull.test.aggregates.routes
  (:require [clojure.test :refer :all]
            [centrebull.test.util :refer [uuid]]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer [parse-body]]
            [centrebull.aggregates.core :as aggregates]
            [ring.util.http-response :as response]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.test.util :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]))

(use-fixtures :once wrap-test)

(deftest test-aggregates-routes
  (testing "Create Aggregate Route - Invalid spec"
    (with-redefs [aggregates/create! (constantly (response/ok {:aggregate-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/aggregates" nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))
    
  (testing "Create Aggregate Route"
    (with-redefs [aggregates/create! (constantly (response/ok {:aggregate-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/aggregates" (gen-aggregate)))]
        (is (= status 200))
        (is (= {:aggregate-create! "called"} (parse-body body)))))))
