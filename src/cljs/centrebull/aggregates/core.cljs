(ns centrebull.aggregates.core
  (:require
    [secretary.core :as secretary]
    [centrebull.components.modal :as modal]
    [centrebull.aggregates.handlers]
    [centrebull.aggregates.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (let [activites (rf/subscribe [:activities])
        competition-id @(rf/subscribe [:active-competition-id])
        new-activity (r/atom {:competition/id competition-id})
        toggle-action #(modal/toggle new-activity)
        reset-action #(modal/toggle new-activity)
        submit-action #(rf/dispatch [:activity-create @new-activity [:refresh-activities] reset-action])
        valid? (fn [] (s/valid? :api/activity-create @new-activity))]
    (fn []
      [:div
       [v/activites-page toggle-action @activites]
       [modal/modal {:state new-activity
                     :title "Register a new Activity"
                     :view  [v/register submit-action valid? new-activity]}]])))

(secretary/defroute "/aggregates" []
  (rf/dispatch [:aggregates-load]))

(def pages
  {:aggregates #'page})
