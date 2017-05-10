(ns centrebull.grand-aggregates.core
  (:require [secretary.core :as secretary]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [centrebull.grand-aggregates.views :as v]
            [clojure.spec :as s]))

(defn- page []
  (let [aggregates (rf/subscribe [:aggregates])
        competition-id @(rf/subscribe [:active-competition-id])
        grand-aggregate (r/atom {:competition/id   competition-id
                                 :activities            []
                                 :aggregate/activities  []
                                 :aggregate/priority    1
                                 :aggregate/description ""})
        ;; CHANGE BELOW
        reset-action #(reset! grand-aggregate {:compeition/id competition-id :activities []})
        ;; CHANGE BELOW
        submit #(rf/dispatch [:aggregate-create @grand-aggregate [:refresh-aggregates] reset-action])
        ;; CHANGE BELOW
        toggle-action (fn [res add?]
                        (if add?
                          (->>
                            (into (:activities @grand-aggregate) [res])
                            distinct
                            (sort-by :activity/priority)
                            (swap! grand-aggregate assoc :activities))
                          (->> @grand-aggregate
                            :activities
                            (filter (partial not= res))
                            (sort-by :activity/priority)
                            (swap! grand-aggregate assoc :activities)))
                        (swap! grand-aggregate assoc :aggregate/activities (mapv :activity/id (:activities @grand-aggregate))))
        ;; CHANGE BELOW
        valid? (fn [] (s/valid? :api/aggregate-create @grand-aggregate))]
    [v/grand-aggregates-page grand-aggregate @aggregates toggle-action submit valid?]))

(def pages
  {:grand-aggregates #'page})

(secretary/defroute "/grand-aggregates" []
  (rf/dispatch [:set-active-page :grand-aggregates]))
