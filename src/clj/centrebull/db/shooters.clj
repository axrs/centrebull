(ns centrebull.db.shooters
  (:require [clojure.string :refer [split lower-case]]
   <<<<<<< HEAD
            [clojure.set :refer [map-invert]]
            [centrebull.db.util :refer [mapper]]
   =======
   >>>>>>> 8a5eefdd226c7ed19f171c7cbc7c1f25fe3bbc42
            [centrebull.db.core :refer [shooters-create! shooters-suggest shooters-find-by-id]]))


(def ^:private key-map {:shooter/sid            :sid
                        :shooter/first-name     :first-name
                        :shooter/last-name      :last-name
                        :shooter/preferred-name :preferred-name
                        :shooter/club           :club})

(def ^:private value-map (map-invert key-map))

(def default-fields {:club           ""
                     :preferred-name ""})

(defn- out-mapper [m] (mapper value-map m))
(defn- in-mapper [m] (mapper key-map m))

(defn create! [shooter]
  (->> shooter
    in-mapper
    (merge default-fields)
    shooters-create!))

(defn- prepare-shooter-search-terms
  "Splits search terms by spaces and wraps the words with '%' signs"
  [s]
  (map #(str "%" % "%") (split s #"\s+")))

(defn suggest [s]
  (->> s
    prepare-shooter-search-terms
    shooters-suggest
    out-mapper))

(defn find-by-id [sid]
  (->> {:sid sid}
       shooters-find-by-id
       out-mapper))
