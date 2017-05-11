(ns centrebull.grand-aggregates.core
  (:require [secretary.core :as secretary]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [centrebull.grand-aggregates.handlers]
            [centrebull.grand-aggregates.views :as v]
            [clojure.spec :as s]))

(defn- page []
  (let [aggregates (rf/subscribe [:aggregates])
        grand-aggregates (rf/subscribe [:grand-aggregates])
        competition-id @(rf/subscribe [:active-competition-id])
        grand-aggregate (r/atom {:competition/id   competition-id
                                 :aggregates            []
                                 :grand-aggregate/aggregates  []
                                 :aggregate/priority    1
                                 :aggregate/description ""})
        reset-action #(reset! grand-aggregate {:compeition/id competition-id :aggregates []})
        remove (fn [id] (rf/dispatch [:grand-aggregates-delete id [:refresh-grand-aggregates]]))
        submit #(rf/dispatch [:grand-aggregate-create @grand-aggregate [:refresh-grand-aggregates] reset-action])
        valid? (fn [] (s/valid? :api/grand-aggregate-create @grand-aggregate))
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
                        (swap! grand-aggregate assoc :grand-aggregate/aggregates (mapv :aggregate/id (:aggregates @grand-aggregate))))]
    (fn []
      [v/grand-aggregates-page
       @aggregates
       @grand-aggregates
       toggle-action
       submit
       valid?
       remove
       grand-aggregate])))
    

(defn- single []
  (let [aggregate (rf/subscribe [:active-grand-aggregate])
        results (rf/subscribe [:active-grand-aggregate-results])]
    (fn []
      [v/grand-aggregate-page @aggregate @results])))

(def pages
  {:grand-aggregates #'page
   :grand-aggregate-page #'single})

(secretary/defroute "/grand-aggregates/:id" {id :id :as params}
  (rf/dispatch [:set-active-grand-aggregate id]))

(secretary/defroute "/grand-aggregates" []
  (rf/dispatch [:grand-aggregates-load]))