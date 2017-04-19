(ns centrebull.activities.views
  (:require
    [centrebull.components.autocomplete :refer [autocomplete]]
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]))

(defn activity-row
  [{:keys [activity/date
           range/description
           activity/priority]}]
  [:div
   [:div {:local "2/12"} priority]
   [:div {:local "5/12"} date]
   [:div {:local "5/12"} description]])

(defn activites-page [toggle-action activities]
  [:section
   [:card
    [:h2 {:local "9/12"} "Activities"]
    [:button {:local "3/12" :on-click toggle-action} "New Activity"]
    (for [act activities]
      ^{:key (:activity/id act)} [activity-row act])]])

(defn register-modal [toggle submit state valid?]
  [:modal {:on-click toggle}
   [:card {:on-click #(.stopPropagation %)}
    [:h2 "Register New Activity"]
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
     "Save"]]])
