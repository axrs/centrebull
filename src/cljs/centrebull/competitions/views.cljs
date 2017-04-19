(ns centrebull.competitions.views
  (:require
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]))

(defn- competition-header [results]
  (when @results
    [:header
     [:caption {:local "3/4"} "Description"]
     [:caption {:local "1/4"} "Dates"]]))

(defn- competition-row [{:keys [competition/id competition/description
                                competition/start-date competition/end-date] :as comp}]
  [:div {:on-click #(rf/dispatch [:set-active-competition comp])}
   [:p {:local "3/4"} description]
   [:p {:local "1/4"} start-date [:br] end-date]])

(defn competitions-page [toggle-action]
  [:section
   [:card
    [:h2 {:local "9/12"} "Competitions"]
    [:button {:local "3/12" :on-click toggle-action} "New Competition"]

    [search "/competitions/search"
     {:header competition-header
      :row    competition-row}]]])

(defn register-modal [state valid? toggle-action submit-action]
  [:modal {:on-click toggle-action}
   [:card {:on-click #(.stopPropagation %)}
    [:h2 "Register New Competition"]
    [:grid
     [input {:title       "Description"
             :grid        "1/1"
             :ratom       state
             :key         :competition/description
             :placeholder "Description"
             :required?   true}]
     [input {:title       "Start Date"
             :grid        "1/2"
             :ratom       state
             :key         :competition/start-date
             :placeholder "YYYY-MM-DD"
             :required?   true}]
     [input {:title       "End Date"
             :grid        "1/2"
             :ratom       state
             :key         :competition/end-date
             :placeholder "YYYY-MM-DD"
             :required?   true}]]
    [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit-action :disabled (not (valid?))} "Save"]]])
