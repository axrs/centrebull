(ns centrebull.db.util
  (:require [clojure.string :refer [split lower-case]]
            [clojure.walk :refer [postwalk]]))

(defn- internal
  "Extract all keys from a map that exist in the ::key-map
  and rename them accordingly returning an updated map"
  [col m]
  (->> m
       (filter #(contains? col (first %)))
       (postwalk #(if (keyword? %) (% col) %))
       (into {})))

(defn mapper [col m]
  (cond
    (seq? m) (into [] (map #(internal col %) m))
    :else (internal col m)))


(defn prepare-search-terms
  "Splits search terms by spaces and wraps the words with '%' signs"
  [s]
  (map #(str "%" % "%") (split s #"\s+")))
