(ns centrebull.test.shooters.routes
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer [parse-body]]
            [centrebull.shooters.core :as shooters]
            [ring.util.http-response :as response]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.test.util :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]
            [cheshire.core :as cheshire]))

(use-fixtures :once wrap-test)

(deftest shooter-routes
  (testing "Create Shooter Route - Invalid spec"
    (with-redefs [shooters/create! (constantly (response/ok {:shooter-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/shooters" nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))

  (testing "Create Shooter Route"
    (with-redefs [shooters/create! (constantly (response/ok {:shooter-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/shooters" (gen-shooter)))]
        (is (= status 200))
        (is (= {:shooter-create! "called"} (parse-body body))))))

  (testing "Search Shooter Route"
    (with-redefs [shooters/suggest (constantly (response/ok {:shooter-suggest "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/shooters/search" {:search/q "asdf"}))]
        (is (= status 200))
        (is (= {:shooter-suggest "called"} (parse-body body))))))

  (testing "Find Shooter by ID Route"
    (with-redefs [shooters/find-by-id (constantly (response/ok {:shooter-find-by-id "called"}))]
      (let [{:keys [status body]} ((app) (request :get "/shooters/1" nil))]
        (is (= status 200))
        (is (= {:shooter-find-by-id "called"} (parse-body body)))))))
