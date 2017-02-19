(ns centrebull.test.shooters.routes
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer [parse-body]]
            [centrebull.shooters.core :as shooters]
            [ring.util.http-response :as response]))

(deftest shooter-routes
  (testing "Create Shooter Route"
    (with-redefs [shooters/create! (constantly (response/ok {:shooter-create! "called"}))]
      (let [{:keys [status body]} ((app) (request :post "/shooters/" nil))]
        (is (= status 200))
        (is (= (parse-body body) {:shooter-create! "called"}))))))