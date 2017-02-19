(ns centrebull.db.shooters
  (:require [clojure.string :refer [split lower-case]]
            [centrebull.db.core :refer [shooters-create! shooters-suggest]]))

(defn create! [{:keys [sid first-name last-name preferred-name club]}]
  (shooters-create! {:sid            sid
                     :first-name     first-name
                     :last-name      last-name
                     :preferred-name preferred-name
                     :club           club}))

(defn- prepare-shooter-search-terms
  "Splits search terms by spaces and wraps the words with '%' signs"
  [s]
  (map #(str "%" % "%") (split s #"\s+")))
