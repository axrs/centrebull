(ns centrebull.grand-aggregates.views
  (:require [centrebull.aggregates.views :refer [aggregate-section aggregate-form]]
            [centrebull.date-utils :refer [format-date]]
            [reagent.core :as r]))

(defn aggregate-table-head []
  [:thead
   [:tr
    [:th "#"]
    [:th "Description"]
    [:th "Actions"]]])

(defn aggregate-row
  [submit {:keys [aggregate/id
                  aggregate/description
                  aggregate/priority] :as aggregate} add?]
  [:tr
   [:td priority]
   [:td description]
   [:td [:button {:on-click #(submit aggregate add?)} (if add? "Add" "Remove")]]])

(defn grand-aggregates-page [grand-aggregate aggregates action submit valid?]
  [:div
    [:section
      [:card
        [:h2 {:local "9/12"} "Grand Aggregates"]]]
    [aggregate-section]
    [:section
      [:card
        [:h4 "Create new grand aggregate"]
        [aggregate-form grand-aggregate]
        [:table
          [aggregate-table-head]
          [:tbody]
          (for [act (:activities @grand-aggregate)]
            ^{:key (:activity/id act)} [aggregate-row action act false])]
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
        (for [aggregate aggregates]
         ^{:key (:aggregate/id aggregate)} [aggregate-row action aggregate true])]]]]])
