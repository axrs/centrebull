(ns centrebull.shooters.view
  (:require [centrebull.components.search :refer [search]]
            [centrebull.components.select :refer [select]]
            [centrebull.components.input :refer [input]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.spec :as s]))

(def grade-list [{:id "" :name "Select a Grade" :disabled? false :selected? true}
                 {:id "A" :name "A"}
                 {:id "B" :name "B"}
                 {:id "C" :name "C"}
                 {:id "FO" :name "FO"}
                 {:id "FS" :name "FS"}])

(defn shooter-row [competition-id]
  (let [grade (r/atom nil)]
    (fn [{:keys [shooter/sid
                 shooter/preferred-name
                 shooter/first-name
                 shooter/last-name
                 shooter/club
                 competition/id] :as shooter}
         results]
      ^{:key sid}
      (let [grade-select (select #(if (= % "Select a Grade") (reset! grade "") (reset! grade %)))
            body {:competition/id competition-id :shooter/grade @grade :shooter/sid sid}
            valid? (s/valid? :api/competition-register-shooter body)]
        [:div
         [:div {:local "1/12"} sid]
         [:div {:local "4/12"} (str first-name
                                 (when preferred-name (str " (" preferred-name ")"))
                                 " " last-name)]
         [:div {:local "3/12"} club]
         [:div {:local "2/12"}
          (if id
            [:h4.registed "Registed - Class " (:shooter/grade shooter)]
            (grade-select grade-list))]
         [:div {:local "2/12"}
          (if id
            [:button {:on-click #(rf/dispatch [:shooters-unregister (:entry/id shooter) results])} "Unregister"]
            [:button {:disabled (not valid?)
                      :on-click #(rf/dispatch [:shooters-register body [:update-registered-shooters body results]])}
             "Register"])]]))))

(defn shooters-page [toggle-action]
  [:section
   [:card
    [:h2 {:local "9/12"} "Shooters"]
    [:button {:local "3/12" :on-click toggle-action} "New Shooter"]
    (let [competition-id @(rf/subscribe [:active-competition-id])
          endpoint (if competition-id "/registrations/search" "/shooters/search")
          atom (r/atom {:competition/id competition-id})]
      [search endpoint {:atom atom :row #(shooter-row competition-id)}])]])

(defn register [submit-action valid? state]
  [:div
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
   [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit-action :disabled (not (valid?))} "Save"]])
