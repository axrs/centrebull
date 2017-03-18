(ns centrebull.shooters.view
  (:require [centrebull.components.search :refer [search]]
            [re-frame.core :as rf]))

(defn shooters-page []
  [:section
   [:card
    [:h2 {:local "9/12"} "Shooters"]
    [search "/shooters/search"
     (fn [{:keys [shooter/sid shooter/preferred-name shooter/first-name shooter/last-name shooter/club]}]
       [:div {:on-click #(rf/dispatch [:set-active-shooter sid])}
        [:div {:local "1/3"} sid]
        [:div {:local "1/3"} (or preferred-name (str first-name " " last-name))]
        [:div {:local "1/3"} club]])]]])
