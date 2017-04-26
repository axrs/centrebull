(ns centrebull.test.mock-generators
  (:require [faker.name :refer :all]
            [clj-time.core :as t]
            [centrebull.test.util :refer [uuid]]
            [faker.phone-number :refer :all]
            [faker.internet :refer :all]
            [clojure.data.generators :refer [string]]
            [clj-time.format :as f]))

(def ^:private date-formatter (f/formatter "yyyy-MM-dd"))

(defn gen-shooter
  "Generates a random shooter with fields similar to the database result
  id [optional] - id to use"
  ([] (gen-shooter (rand-int 99999)))
  ([id] {:shooter/sid            id
         :shooter/first-name     (first-name)
         :shooter/last-name      (last-name)
         :shooter/preferred-name (last-name)
         :shooter/club           (string)}))

(defn gen-range
  ([] (gen-range (uuid)))
  ([id] {:range/id          id
         :range/description (string)}))

(defn- random-date-string []
  (let [year (+ 1970 (rand-int 20))
        month (+ 1 (rand-int 11))
        month (if (> 10 month) (str "0" month) month)
        day (+ 1 (rand-int 27))
        day (if (> 10 day) (str "0" day) day)]
    (str year "-" month "-" day)))

(defn gen-competition
  "Generates a random competition object with fields similar to the database result
  id [optional] - UUID to use instead"
  ([] (gen-competition (uuid)))
  ([id] {:competition/id          id
         :competition/description (string)
         :competition/start-date  (random-date-string)
         :competition/end-date    (random-date-string)}))

(defn gen-competition-regester-request
  "Generates the data needed for a competition register request
  competition-id [optional] - UUID to use instead"
  ([] (gen-competition-regester-request (uuid)))
  ([competition-id] {:competition/id competition-id
                     :shooter/sid    (rand-int 99999)
                     :shooter/grade  (string)}))

(defn gen-activity
  "Generates a random activity map with fields similar to the database result
  id [optional] - UUID to use instead"
  ([] (gen-activity (uuid)))
  ([id] {:activity/id       id
         :competition/id    (uuid)
         :activity/range-id (uuid)
         :activity/priority (clojure.data.generators/int)
         :activity/date     (random-date-string)}))

(defn gen-search-query
  "Generates a search query"
  []
  {:search/q       (string)
   :competition/id (uuid)})

(defn gen-shooters-registered
  "Generates a shooter with registration details"
  []
  {:competition/id          (uuid)
   :competition/description (string)
   :competition/start-date  (random-date-string)
   :competition/end-date    (random-date-string)
   :shooter/last-name       (string)
   :shooter/first-name      (string)
   :shooter/preferred-name  (string)
   :shooter/grade           (string)
   :shooter/club            (string)
   :shooter/sid             (rand-int 99999)})

(defn gen-entry
  "Generates an entry/registrations"
  []
  {:entry/id (uuid)
   :competition/id (uuid)
   :shooter/sid (rand-int 99999)
   :shooter/grade (string)})

(defn gen-registration-with-shooter
  "Generates a entry.registraions with a shooter"
  []
  (merge (gen-entry) (gen-shooter)))

(defn gen-result
 "Generates a result"
  []
  {:result/id    (uuid)
   :result/vs    (rand-int 10)
   :result/score (rand-int 50)
   :result/shots (string)
   :shooter/sid  (rand-int 99999)
   :activity/id  (uuid)})
