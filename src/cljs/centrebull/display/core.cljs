(ns centrebull.display.core
  (:require
    [secretary.core :as secretary]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [centrebull.display.views :as v]))

(defn set-timeout []
  (js/setInterval #(rf/dispatch [:update-tv]) (* 5 1000)))

(defonce timeout (set-timeout))

(defn- page []
  (let [results @(rf/subscribe [:tv-results])
        aggregate-priorities (map :aggregate/priority @(rf/subscribe [:aggregates]))
        grouped-results (group-by :shooter/grade results)
        right-col (dissoc grouped-results "FO" "FTR" "FSA" "FSB")
        left-col (dissoc grouped-results "A" "B" "C")
        f (first results)
        pri (apply sorted-set (into #{} (map :aggregate/priority (:aggregate/results f))))]
    [v/display-page results aggregate-priorities grouped-results right-col left-col pri]))

(secretary/defroute "/tv" []
  (rf/dispatch [:bull-clicked]))

(def pages
  {:tv #'page})
