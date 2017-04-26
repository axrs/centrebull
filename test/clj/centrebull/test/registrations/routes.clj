(ns centrebull.test.registrations.routes
  (:require [clojure.test :refer :all]
    [centrebull.test.util :refer [uuid]]
    [ring.mock.request :refer :all]
    [centrebull.handler :refer :all]
    [centrebull.test.util :refer [parse-body]]
    [centrebull.registrations.core :as registrations]
    [ring.util.http-response :as response]
    [centrebull.test.mock-generators :refer :all]
    [centrebull.test.util :refer :all]
    [centrebull.test.wrapper :refer [wrap-test]]))

(use-fixtures :once wrap-test)

(deftest registrations-routes
    (testing "Register Shooter Route"
      (with-redefs [registrations/register-shooter! (constantly (response/ok {:register-shooter "called"}))]
        (let [{:keys [status body]} ((app) (json-request :post "/registrations" (gen-entry)))]
          (is (= status 200))
          (is (= {:register-shooter "called"} (parse-body body))))))

    (testing "Unregister shooter route"
      (with-redefs [registrations/unregister-shooter! (constantly (response/ok {:unregister-shooter "called"}))]
        (let [{:keys [status body]} ((app) (json-request :delete (str "/registrations/" (uuid))))]
          (is (= status 200))
          (is (= {:unregister-shooter "called"} (parse-body body))))))

    (testing "Search Registrations"
      (with-redefs [registrations/suggest-registration (constantly (response/ok {:suggest-registration "called"}))]
        (let [{:keys [status body]} ((app) (json-request :post "/registrations/search"
                                                         {:search/q "asdf"
                                                          :competition/id (uuid)}))]
          (is (= status 200))
          (is (= {:suggest-registration "called"} (parse-body body)))))))