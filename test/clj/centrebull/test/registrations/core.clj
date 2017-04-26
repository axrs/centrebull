(ns centrebull.test.registrations.core
  (:require [clojure.test :refer :all]
            [centrebull.test.mock-generators :refer :all]
            [centrebull.db.entries :as dao]
            [centrebull.registrations.core :refer :all]
            [centrebull.test.db.entries :as mock]
            [centrebull.registrations.core :as registrations]))

(deftest registrations
  (let [expected (gen-entry)
        request {:all-params expected}]
      (testing "register shooter with existing registration"
        (with-redefs [dao/find (mock/find expected expected)
                      dao/update-active! (mock/update-active! (:entry/id expected) true)
                      dao/create! mock/do-not-call]
          (let [{:keys [status body]} (register-shooter! request)]
            (is (= body expected))
            (is (= status 200)))))

      (testing "register shooter without existing registration"
        (with-redefs [dao/find (mock/find expected nil)
                      dao/update-active! mock/do-not-call
                      dao/create! (mock/create! expected)]
          (let [{:keys [status body]} (register-shooter! request)]
            (is (= body expected))
            (is (= status 200)))))

      (testing "unregister shooter"
        (with-redefs [dao/update-active! (mock/update-active! (:entry/id expected) false)]
          (let [{:keys [status]} (registrations/unregister-shooter! request)]
            (is (= status 200))))))

  (testing "suggest registration"
    (let [expected (gen-shooters-registered)
          expected-input (gen-search-query)
          id (:competition/id expected-input)
          q (:search/q expected-input)]
      (with-redefs [dao/suggest-registration (mock/suggest-registration q id expected)]
        (let [{:keys [status body]} (registrations/suggest-registration {:all-params expected-input})]
          (is (= body expected))
          (is (= status 200)))))))