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

(defn- single-activity []
  (let [act @(rf/subscribe [:active-activity])
        results @(rf/subscribe [:active-activity-results])
        show-modal? (r/atom false)
        new (r/atom {})
        toggle-action #(rf/dispatch [:toggle show-modal? new])
        submit-action #(rf/dispatch [:activity-create-result @new [[:refresh-activity-results] [:toggle show-modal? new]]])
        valid? (fn [] (s/valid? :api/result-create @new))]
    (fn []
      [:div
       [v/single-activity-page toggle-action act results]
       (when @show-modal?
         (swap! new assoc :activity/id (:activity/id act) :shooter/sid 123)
         [v/register-result-modal toggle-action submit-action new valid?])])))

(secretary/defroute "/activities" []
  (rf/dispatch [:activities-load]))

(secretary/defroute "/activities/:id" {id :id :as params}
  (rf/dispatch [:set-active-activity id]))

(def pages
  {:activities #'page
   :activity   #'single-activity})
