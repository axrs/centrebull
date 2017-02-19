(ns centrebull.db.shooters
  (:require [centrebull.db.core :refer [shooters-create!]]))

(defn create! [{:keys [sid first-name last-name preferred-name club]}]
  (shooters-create! {:sid            sid
                     :first-name     first-name
                     :last-name      last-name
                     :preferred-name preferred-name
                     :club           club}))