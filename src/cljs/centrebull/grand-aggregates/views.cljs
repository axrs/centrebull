(ns centrebull.grand-aggregates.views
  (:require [centrebull.aggregates.views :refer [aggregate-form agg-row]]
            [centrebull.date-utils :refer [format-date]]
            [reagent.core :as r]))

(defn aggregate-row
  [submit {:keys [aggregate/id
                  aggregate/description
                  aggregate/priority] :as aggregate} add?]
  [:tr
   [:td priority]
   [:td description]
   [:td [:button {:on-click #(submit aggregate add?)} (if add? "Add" "Remove")]]])

(defn aggregate-table-head []
  [:thead
   [:tr
    [:th "#"]
    [:th "Description"]
    [:th "Actions"]]])

(defn grand-aggregate-row
  [remove {:keys [grand-aggregate/id
                  aggregate/priority
                  aggregate/description :as act]}]
  [:tr
   [:td priority]
   [:td description]
   [:td [:button {:on-click #(remove id)} "Delete"]]])

(defn grand-aggregate-section [grand-aggregates remove]
  [:section
   [:card
    [:table
     [:thead
      [:tr
       [:th "#"]
       [:th "Description"]
       [:th ""]]]
     [:tbody
      (for [agg grand-aggregates]
        ^{:key (:grand-aggregate/id agg)} [grand-aggregate-row remove agg])]]]])

(defn grand-aggregates-page [aggregates grand-aggregates action submit valid? remove grand-aggregate]
  [:div
   [:section
    [:card
     [:h2 {:local "9/12"} "Grand Aggregates"]]]
   [grand-aggregate-section grand-aggregates remove]
   [:section
    [:card
     [:h4 "Create new grand aggregate"]
     [aggregate-form grand-aggregate]
     [:table
      [aggregate-table-head]
      [:tbody
       (for [aggregate (:aggregates @grand-aggregate)]
         ^{:key (str "grand-agg" (:aggregate/id aggregate))} [aggregate-row action aggregate false])]]
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
        [:tr
         [:th ""]
         [:th "Name"]
         (for [r (:aggregate/results f)]
           ^{:key (str "grand-agg" (:aggregate/priority r))} [:th "#" (:aggregate/priority r)])
         [:thh "Total"]]]
       [:tbody
        (for [s results]
          ^{:key (str "grand-agg" (:shooter/sid s))} [agg-row pri s])]]]]))
