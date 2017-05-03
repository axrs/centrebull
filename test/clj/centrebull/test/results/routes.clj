(ns centrebull.test.results.routes
  (:require [clojure.test :refer :all]
            [centrebull.test.wrapper :refer [wrap-test]]
            [centrebull.test.util :refer :all]
            [centrebull.handler :refer :all]
            [centrebull.results.core :as results]
            [centrebull.test.mock-generators :refer :all]
            [ring.util.http-response :as response]))

(use-fixtures :once wrap-test)

(deftest results-routes
  (testing "Submit Results Route"
    (with-redefs [results/create! (constantly (response/ok {:create-result "called"}))]
      (let [{:keys [status body]} ((app) (json-request :post "/results" (gen-result)))]
        (is (= status 200))
        (is (= {:create-result "called"} (parse-body body)))))))
