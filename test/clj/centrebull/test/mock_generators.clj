(ns centrebull.test.mock-generators
  (:require [faker.name :refer :all]
            [faker.phone-number :refer :all]
            [faker.internet :refer :all]
            [clojure.data.generators :refer [string]]))

(defn gen-shooter
  "Generates a random shooter with fields similar to the database result
  id [optional] - id to use"
  ([] (gen-shooter (rand-int 99999)))
  ([id] {:shooter/sid            id
         :shooter/first-name     (first-name)
         :shooter/last-name      (last-name)
         :shooter/preferred-name (last-name)
         :shooter/club           (string)}))
