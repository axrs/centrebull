(ns centrebull.ranges.views
  (:require
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]))

(defn- ranges-header [results]
  [:thead
   [:tr
    [:th "Description"]]])

(defn- ranges-row [{:keys [range/id range/description]}]
  [:tr
   [:td description]])


(defn ranges-page [toggle-action]
  [:section
   [:card
    [:h2 {:local "9/12"} "Ranges"]
    [:button {:local "3/12" :on-click toggle-action} "New Range"]

    [search "/ranges/search"
     {:header ranges-header
      :row    ranges-row}]]])

(defn register [submit-action valid? state]
  [:div
   [:grid
    [input {:title       "Description"
            :grid        "1/1"
            :ratom       state
            :key         :range/description
            :placeholder "Description"
            :required?   true}]]
   [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit-action :disabled (not (valid?))} "Save"]])
