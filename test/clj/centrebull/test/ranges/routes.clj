(ns centrebull.test.ranges.routes
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.test.util :refer :all]
            [centrebull.ranges.core :as ranges]
            [ring.util.http-response :as response]
            [centrebull.test.ranges.core :refer :all]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]))

(use-fixtures :once wrap-test)

(deftest range-routes
  (testing "Create Range Route - Invalid spec"
    (with-redefs [ranges/create! (constantly (response/ok {:range-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/ranges" nil))]
        (is (= status 400))
        (is (contains? (parse-body body) :errors)))))

  (testing "Create Range Route"
    (with-redefs [ranges/create! (constantly (response/ok {:range-create! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/ranges" (gen-range)))]
        (is (= status 200))
        (is (= {:range-create! "called"} (parse-body body))))))

  (testing "Delete Range Route"
    (with-redefs [ranges/delete! (constantly (response/ok {:range-delete! "called"}))]
      (let [{:keys [status body]} ((app) (json-request :delete (str "/ranges/" (uuid))))]
        (is (= status 200))
        (is (= {:range-delete! "called"} (parse-body body))))))

  (testing "Search Range Route"
    (with-redefs [ranges/suggest (constantly (response/ok {:range-suggest "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/ranges/search" {:search/q "asdf"}))]
        (is (= status 200))
        (is (= {:range-suggest "called"} (parse-body body)))))))
