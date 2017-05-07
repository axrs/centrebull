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
      (let [{:keys [status body]} ((app) (json-request :post (str "/competitions/" (uuid) "/aggregates") nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))
    
  (testing "Create Aggregate Route"
    (with-redefs [aggregates/create! (constantly (response/ok {:aggregate-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post (str "/competitions/" (uuid) "/aggregates") (gen-aggregate)))]
        (is (= status 200))
        (is (= {:aggregate-create! "called"} (parse-body body))))))

  (testing "Find Aggregate"
    (let [competition-id (uuid)]
      (with-redefs [aggregates/find-aggregates (constantly (response/ok {:aggregate-find "called"}))]
        (let [{:keys [status body]} ((app) (json-request :get (str "/competitions/" competition-id "/aggregates")))]
          (is (= status 200))
          (is (= {:aggregate-find "called"} (parse-body body)))))))

  (testing "Delete Aggregate"
    (let [competition-id (uuid)
          aggregate-id (uuid)]
      (with-redefs [aggregates/delete-aggregate! (constantly (response/ok {:aggregate-delete! "called"}))]
        (let [{:keys [status body]} ((app) (json-request :delete (str "/competitions/" competition-id "/aggregates/" aggregate-id)))]
          (is (= status 200))
          (is (= {:aggregate-delete! "called"} (parse-body body))))))))
