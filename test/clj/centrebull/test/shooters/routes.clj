(ns centrebull.test.shooters.routes
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer [parse-body]]
            [centrebull.shooters.core :as shooters]
            [ring.util.http-response :as response]
            [centrebull.test.wrapper :refer [wrap-test]]))

(use-fixtures :once wrap-test)

(deftest shooter-routes
  (testing "Create Shooter Route"
    (with-redefs [shooters/create! (constantly (response/ok {:shooter-create! "called"}))]
      (let [{:keys [status body]} ((app) (request :post "/shooters/" nil))]
        (is (= status 200))
        (is (= (parse-body body) {:shooter-create! "called"})))))

  (testing "Search Shooter Route"
    (with-redefs [shooters/suggest (constantly (response/ok {:shooter-suggest "called"}))]
      (let [{:keys [status body]} ((app) (request :get "/shooters/search?q=asdf" nil))]
        (is (= status 200))
        (is (= (parse-body body) {:shooter-suggest "called"})))))

  (testing "Find Shooter by ID Route"
    (with-redefs [shooters/find-by-id (constantly (response/ok {:shooter-find-by-id "called"}))]
      (let [{:keys [status body]} ((app) (request :get "/shooters/1" nil))]
        (is (= status 200))
        (is (= (parse-body body) {:shooter-find-by-id "called"}))))))
