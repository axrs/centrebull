(ns centrebull.shooters.view
  (:require [centrebull.components.search :refer [search]]
            [centrebull.components.input :refer [input]]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn shooter-row [competition-id]
  (fn [{:keys [shooter/sid
               shooter/preferred-name
               shooter/first-name
               shooter/last-name
               shooter/club
               competition/id] :as shooter}
       results]
    [:div
     [:div {:local "1/12"} sid]
     [:div {:local "4/12"} (str first-name
                             (when preferred-name (str " (" preferred-name ")"))
                             " " last-name)]
     [:div {:local "4/12"} club]
     [:div {:local "3/12"}
      (if id [:h4.registed "Registed"]
             [:button {:on-click #(let [body {:shooter/sid    sid
                                              :shooter/grade  (js/prompt "Shooter Grade")
                                              :competition/id competition-id}]
                                    (rf/dispatch [:shooters-register body results (r/atom {}) [[:update-registered-shooters body results]]]))}
              "Register"])]
     [:div {:local "1/12"}
      (when id
        [:button {:on-click #(rf/dispatch [:shooters-unregister (:entry/id shooter) results])}
         "Unregister"])]]))

(defn shooters-page [toggle-action]
  [:section
   [:card
    [:h2 {:local "9/12"} "Shooters"]
    [:button {:local "3/12" :on-click toggle-action} "New Shooter"]
    (let [competition-id @(rf/subscribe [:active-competition-id])
          endpoint (if competition-id "/registrations/search" "/shooters/search")
          atom (r/atom {:competition/id competition-id})]
      [search endpoint {:atom atom :row (shooter-row competition-id)}])]])

(defn register-modal [state valid? toggle-action submit-action]
  [:modal {:on-click toggle-action}
   [:card {:on-click #(.stopPropagation %)}
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
