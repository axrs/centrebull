(ns centrebull.grand-aggregates.core
  (:require [secretary.core :as secretary]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [centrebull.grand-aggregates.handlers]
            [centrebull.grand-aggregates.views :as v]
            [clojure.spec :as s]))

(defn- page []
  (let [aggregates (rf/subscribe [:aggregates])
        competition-id @(rf/subscribe [:active-competition-id])
        grand-aggregate (r/atom {:competition/id   competition-id
                                 :aggregates            []
                                 :grand-aggregate/aggregates  []
                                 :aggregate/priority    1
                                 :aggregate/description ""})
        reset-action #(reset! grand-aggregate {:compeition/id competition-id :aggregates []})
        ;; CHANGE BELOW
        submit #(rf/dispatch [:grand-aggregate-create @grand-aggregate [:refresh-grand-aggregates] reset-action])
        ;; CHANGE BELOW
        toggle-action (fn [res add?]
                        (if add?
                          (->>
                            (into (:aggregates @grand-aggregate) [res])
                            distinct
                            (sort-by :aggregate/priority)
                            (swap! grand-aggregate assoc :aggregates))
                          (->> @grand-aggregate
                            :aggregates
                            (filter (partial not= res))
                            (sort-by :aggregate/priority)
                            (swap! grand-aggregate assoc :aggregates)))
                        (swap! grand-aggregate assoc :grand-aggregate/aggregates (mapv :aggregate/id (:aggregates @grand-aggregate))))
        valid? (fn [] (s/valid? :api/grand-aggregate-create @grand-aggregate))]
    [v/grand-aggregates-page grand-aggregate @aggregates toggle-action submit valid?]))

(def pages
  {:grand-aggregates #'page})

; (secretary/defroute "/grand-aggregates" []
;   (rf/dispatch [:set-active-page :grand-aggregates]))

(secretary/defroute "/grand-aggregates" []
  (rf/dispatch [:grand-aggregates-load]))