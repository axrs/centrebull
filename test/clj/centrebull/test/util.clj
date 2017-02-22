(ns centrebull.test.util
  (:require [cheshire.core :as json]))

(defn parse-body [b]
  (json/parse-string (slurp b) true))