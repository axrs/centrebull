(ns centrebull.shooters.view
  (:require [centrebull.components.search :refer [search]]
            [centrebull.components.input :refer [input]]
            [re-frame.core :as rf]))

(defn shooters-page [toggle-action]
  [:section
   [:card
    [:h2 {:local "9/12"} "Shooters"]
    [:button {:local "3/12" :on-click toggle-action} "New Shooter"]

    [search "/shooters/search"
     (fn [{:keys [shooter/sid shooter/preferred-name shooter/first-name shooter/last-name shooter/club]}]
       (let [name (if (empty? preferred-name) (str first-name " " last-name) preferred-name)]
         [:div {:on-click #(rf/dispatch [:set-active-shooter sid])}
          [:div {:local "1/3"} sid]
          [:div {:local "1/3"} name]
          [:div {:local "1/3"} club]]))]]])

(defn register-modal [state valid? toggle-action submit-action]
  [:modal {:on-click toggle-action}
   [:card {:on-click toggle-action}
    [:h2 "Register New Shooter"]
    [:grid
     [input {:title       "Sid"
             :grid        "1/3"
             :ratom       state
             :key         :shooter/sid
             :placeholder "sid"
             :required?   true}]
     [input {:title       "First Name"
             :grid        "1/3"
             :ratom       state
             :key         :shooter/first-name
             :placeholder "First Name"
             :required?   true}]
     [input {:title       "Last Name"
             :grid        "1/3"
             :ratom       state
             :key         :shooter/last-name
             :placeholder "Last Name"
             :required?   true}]
     [input {:title       "Preferred name"
             :grid        "1/3"
             :ratom       state
             :key         :shooter/preferred-name
             :placeholder "Preferred Name"
             :required?   false}]
     [input {:title       "Club"
             :grid        "1/3"
             :ratom       state
             :key         :shooter/club
             :placeholder "Club"
             :required?   false}]]
    [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit-action :disabled (not (valid?))} "Save"]]])
