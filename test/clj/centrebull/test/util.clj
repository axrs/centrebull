(ns centrebull.test.util
  (:require [cheshire.core :as json]
            [ring.mock.request :refer :all])
  (:import (java.util UUID)))

(defn uuid
  "Generates a random UUID"
  [] (str (UUID/randomUUID)))

(defn parse-body [b]
  (json/parse-string (slurp b) true))

(defn json-request
  "Prepares a JSON request for testing"
  ([method uri] (json-request method uri nil))
  ([method uri body]
   (-> (request method uri (json/generate-string body))
       (content-type "application/json"))))
