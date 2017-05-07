(ns centrebull.aggregates.views
  (:require
    [centrebull.components.autocomplete :refer [autocomplete]]
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [centrebull.date-utils :refer [format-date]]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [cljs.spec :as s]
    [clojure.string :as string]))

(defn activity-row
  [submit {:keys [activity/id
                  activity/date
                  range/description
                  activity/priority] :as act} add?]
  [:tr
   [:td priority]
   [:td description]
   [:td (format-date date)]
   [:td [:button {:on-click #(submit act add?)} (if add? "Add" "Remove")]]])

(defn aggregate-form [state]
  [:grid
   [input {:title       "Description"
           :grid        "3/4"
           :ratom       state
           :key         :aggregate/description
           :placeholder "Description"
           :required?   true}]
   [input {:title       "Priority"
           :grid        "1/4"
           :ratom       state
           :key         :aggregate/priority
           :placeholder "1"
           :required?   true}]])

(defn aggregate-table-head []
  [:thead
   [:th "#"]
   [:th "Range"]
   [:th "Date"]
   [:th "Actions"]])

(defn aggregate-row
  [remove {:keys [aggregate/id
                  aggregate/priority
                  aggregate/description :as act]}]
  [:tr
   [:td priority]
   [:td description]
   [:td [:button {:on-click #(remove id)} "Delete"]]])

(defn aggregate-section [aggregates remove]
  [:section
   [:card
    [:table
     [:thead
      [:th "#"]
      [:th "Description"]
      [:th ""]]
     [:tbody
      (for [agg aggregates]
        ^{:key (:aggregate/id agg)} [aggregate-row remove agg])]]]])

(defn aggregates-page [activities aggregates action submit valid? remove aggregated]
  [:div
   [:section
    [:card
     [:h2 {:local "9/12"} "Aggregates"]]]
   [aggregate-section aggregates remove]
   [:section
    [:card
     [:h4 "Create new Activity"]
     [aggregate-form aggregated]
     [:table
      [aggregate-table-head]
      [:tbody
       (for [act (:activities @aggregated)]
         ^{:key (:activity/id act)} [activity-row action act false])]]
     [:button {:data-pull-left "9/12"
               :local          "3/12"
               :data-m-full    ""
               :data-primary   ""
               :on-click       submit
               :disabled       (not (valid?))} "Submit"]]]
   [:section
    [:card
     [:table
      [aggregate-table-head]
      [:tbody
       (for [act activities]
         ^{:key (:activity/id act)} [activity-row action act true])]]]]])

(defn register [submit valid? state]
  [:div
   [:grid
    [autocomplete "/ranges/search" {:title "Range" :k :range/description :on-select #(swap! state assoc :activity/range-id (:range/id %))}]
    [input {:title       "Date"
            :grid        "1/2"
            :ratom       state
            :key         :activity/date
            :placeholder "YYYY-MM-DD"
            :required?   true}]
    [input {:title       "Priority"
            :grid        "1/2"
            :ratom       state
            :key         :activity/priority
            :placeholder "0"
            :required?   true}]]
   [:button {:data-pull-left "9/12"
             :local          "3/12"
             :data-m-full    ""
             :data-primary   ""
             :on-click       submit
             :disabled       (not (valid?))}
    "Save"]])