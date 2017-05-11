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
        aggregates (rf/subscribe [:aggregates])
        competition-id @(rf/subscribe [:active-competition-id])
        aggregated (r/atom {:competition/id        competition-id
                            :activities            []
                            :aggregate/activities  []
                            :aggregate/priority    1
                            :aggregate/description ""})
        reset-action #(reset! aggregated {:compeition/id competition-id :activities []})
        remove (fn [id] (rf/dispatch [:activities-delete id [:refresh-aggregates]]))
        submit #(rf/dispatch [:aggregate-create @aggregated [:refresh-aggregates] reset-action])
        valid? (fn [] (s/valid? :api/aggregate-create @aggregated))
        toggle-action (fn [res add?]
                        (if add?
                          (->>
                            (into (:activities @aggregated) [res])
                            distinct
                            (sort-by :activity/priority)
                            (swap! aggregated assoc :activities))
                          (->> @aggregated
                            :activities
                            (filter (partial not= res))
                            (sort-by :activity/priority)
                            (swap! aggregated assoc :activities)))
                        (swap! aggregated assoc :aggregate/activities (mapv :activity/id (:activities @aggregated))))]
    (fn []
      [v/aggregates-page
       @activites
       @aggregates
       toggle-action
       submit
       valid?
       remove
       aggregated])))

(defn- single []
  (let [aggregate (rf/subscribe [:active-aggregate])
        results (rf/subscribe [:active-aggregate-results])]
    (fn []
      [v/aggregate-page @aggregate @results])))

(secretary/defroute "/aggregates" []
  (rf/dispatch [:aggregates-load]))

(secretary/defroute "/aggregates/:id" {id :id :as params}
  (rf/dispatch [:set-active-aggregate id]))

(def pages
  {:aggregates #'page
   :aggregate  #'single})
