(ns centrebull.test.competitions.routes
  (:require [clojure.test :refer :all]
            [centrebull.test.util :refer [uuid]]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer [parse-body]]
            [centrebull.competitions.core :as competitions]
            [ring.util.http-response :as response]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.test.util :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]))

(use-fixtures :once wrap-test)

(deftest test-competition-routes
  (testing "Create Competition Route - Invalid spec"
    (with-redefs [competitions/create! (constantly (response/ok {:competition-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/competitions" nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))

  (testing "Create Competition Route"
    (with-redefs [competitions/create! (constantly (response/ok {:competition-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/competitions" (gen-competition)))]
        (is (= status 200))
        (is (= {:competition-create! "called"} (parse-body body))))))

  (testing "Find Competition Route"
    (with-redefs [competitions/find (constantly (response/ok {:competition-find "called"}))]
      (let [{:keys [status body]} ((app) (json-request :get (str "/competitions/" (uuid))))]
        (is (= status 200))
        (is (= {:competition-find "called"} (parse-body body))))))

  (testing "Delete Competition Route"
    (with-redefs [competitions/delete! (constantly (response/ok {:competition-delete! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :delete (str "/competitions/" (uuid))))]
        (is (= status 200))
        (is (= {:competition-delete! "called"} (parse-body body))))))

  (testing "Find Competition Activities Route"
    (with-redefs [competitions/find-activities (constantly (response/ok {:competition-find-activites "called"}))]
      (let [{:keys [status body]} ((app) (json-request :get (str "/competitions/" (uuid) "/activities")))]
        (is (= status 200))
        (is (= {:competition-find-activites "called"} (parse-body body))))))

  (testing "Search Competition Route"
    (with-redefs [competitions/suggest (constantly (response/ok {:competition-suggest "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/competitions/search" {:search/q "asdf"}))]
        (is (= status 200))
        (is (= {:competition-suggest "called"} (parse-body body)))))))
