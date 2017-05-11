(ns centrebull.grand-aggregates.views
  (:require [centrebull.aggregates.views :refer [aggregate-section aggregate-form agg-row]]
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
          (for [aggregate (:aggregates @grand-aggregate)]
            ^{:key (:grand-aggregate/id aggregate)} [aggregate-row action aggregate false])]
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

(defn grand-aggregate-page [{:keys [aggregate/description aggregate/priority]} results]
    (let [f (first results)
          pri (mapv :aggregate/priority (:aggregate/results f))]
      [:section
        [:card
          [:h2 description [:sub "#" priority]]
          [:h3 "Aggregate Results"]]
        [:card
          [:table
            [:thead
              [:tr]
              [:th "Grade"]
              [:th "Name"]
              [:th "Club"]
              (for [r (:aggregate/results f)]
                [:th "#" (:aggregate/priority r)])
              [:th "Total"]]
            [:tbody
              (for [s results]
                ^{:key (:shooter/sid s)} [agg-row pri s])]]]]))
