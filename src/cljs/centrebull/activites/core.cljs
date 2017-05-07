(ns centrebull.activities.core
  (:require
    [secretary.core :as secretary]
    [centrebull.components.modal :as modal]
    [centrebull.activities.handlers]
    [centrebull.activities.views :as v]
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

(defn- set-sid-fn [state]
  (fn [sid name]
    (swap! state assoc :shooter/sid sid)
    (swap! state assoc :shooter/name name)
    (modal/toggle state)))

(defn- single-activity []
  (let [act (rf/subscribe [:active-activity])
        act-id (:activity/id @act)
        results (rf/subscribe [:active-activity-results])
        new-result (r/atom {:activity/id act-id})
        toggle-action #(modal/toggle new-result)
        reset-action #(reset! new-result {:activity/id act-id})
        row-click (set-sid-fn new-result)]
    (fn []
      (let [submit-action #(rf/dispatch [:activity-create-result @new-result [:refresh-activity-results] reset-action])
            valid? (fn [] (s/valid? :api/result-create @new-result))]
        [:div
         [(v/single-activity-page row-click @act results)]
         [modal/modal {:state new-result
                       :title "Register Result"
                       :view  [v/register-result submit-action valid? new-result]}]]))))

(secretary/defroute "/activities" []
  (rf/dispatch [:activities-load]))

(secretary/defroute "/activities/:id" {id :id :as params}
  (rf/dispatch [:set-active-activity id]))

(def pages
  {:activities #'page
   :activity   #'single-activity})
