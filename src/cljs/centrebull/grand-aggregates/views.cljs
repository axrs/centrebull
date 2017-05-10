(ns centrebull.grand-aggregates.views
  (:require [centrebull.aggregates.views :refer [aggregate-section aggregate-form]]
            [centrebull.date-utils :refer [format-date]]))

(defn aggregate-table-head []
  [:thead
   [:tr
    [:th "#"]
    [:th "Description"]
    [:th "Actions"]]])

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

(defn grand-aggregates-page [aggregates action submit valid? aggregated]
  [:div
    [:section
      [:card
        [:h2 {:local "9/12"} "Grand Aggregates"]]]
    [aggregate-section]
    [:section
      [:card
        [:h4 "Create new grand aggregate"]
        [aggregate-form aggregated]
        [:table
          [aggregate-table-head]
          [:tbody]
          (for [act (:activities @aggregated)]
            ^{:key (:activity/id act)} [activity-row action act false])]
        [:button {:data-pull-left "9/12"
                  :local          "3/12"
                  :data-m-full    ""
                  :data-primary   ""
                  :on-click       submit
                  :disabled       (not (valid?))} "Submit"]]]
    [:section
     [:card
      [:table
       [aggregate-table-head
        [:tbody
         (for [aggregate aggregates]
          [activity-row action aggregate true])]]]]]])
