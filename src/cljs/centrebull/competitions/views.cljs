(ns centrebull.competitions.views
  (:require [centrebull.components.search :refer [search]]))

(defn competitions-page []
  [:div
   [:h1 "Competitions"]
   [search "/competitions/search" #([:h1 %])]])
