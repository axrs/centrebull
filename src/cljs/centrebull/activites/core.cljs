(ns centrebull.activities.core
  (:require
    [secretary.core :as secretary]
    [centrebull.activities.handlers]
    [centrebull.activities.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (let [activites (rf/subscribe [:activities])
        show-modal? (r/atom false)
        competition-id @(rf/subscribe [:active-competition-id])
        new (r/atom {})
        toggle-action #(rf/dispatch [:toggle show-modal? new])
        submit-action #(rf/dispatch [:activity-create @new [[:refresh-activities] [:toggle show-modal? new]]])
        valid? (fn [] (s/valid? :api/activity-create @new))]
    (fn []
      [:div
       [v/activites-page toggle-action @activites]
       (when @show-modal?
         (swap! new assoc :competition/id competition-id :activity/priority 0)
         [v/register-modal toggle-action submit-action new valid?])])))

(secretary/defroute "/activities" []
  (rf/dispatch [:activities-load]))

(def pages
  {:activities #'page})
