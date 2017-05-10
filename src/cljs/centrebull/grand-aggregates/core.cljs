(ns centrebull.grand-aggregates.core
  (:require [secretary.core :as secretary]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [centrebull.grand-aggregates.views :as v]
            [clojure.spec :as s]))

(defn- page []
  (let [activites (rf/subscribe [:activities])
        competition-id @(rf/subscribe [:active-competition-id])
        aggregated (r/atom {:competition/id        competition-id
                            :activities            []
                            :aggregate/activities  []
                            :aggregate/priority    1
                            :aggregate/description ""})
        ;; CHANGE BELOW
        reset-action #(reset! aggregated {:compeition/id competition-id :activities []})
        ;; CHANGE BELOW
        submit #(rf/dispatch [:aggregate-create @aggregated [:refresh-aggregates] reset-action])
        ;; CHANGE BELOW
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
                        (swap! aggregated assoc :aggregate/activities (mapv :activity/id (:activities @aggregated))))
        ;; CHANGE BELOW
        valid? (fn [] (s/valid? :api/aggregate-create @aggregated))]
    (v/grand-aggregates-page activites toggle-action submit valid? aggregated)))

(def pages
  {:grand-aggregates #'page})

(secretary/defroute "/grand-aggregates" []
  (rf/dispatch [:set-active-page :grand-aggregates]))
