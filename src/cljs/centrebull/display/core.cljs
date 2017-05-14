(ns centrebull.display.core
  (:require
    [secretary.core :as secretary]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [centrebull.display.views :as views]))

(defn set-timeout []
  (js/setInterval #(rf/dispatch [:update-tv]) (* 5 1000)))

(defonce timeout (set-timeout))

(defn agg-row [pri aggs {:keys [rank shooter/grade shooter/first-name shooter/last-name shooter/club shooter/sid aggregate/results aggregate/score aggregate/vs]}]
  [:tr
   [:td rank]
   [:td first-name " " last-name]
   (for [r pri]
     (let [res (first (filter #(= r (:aggregate/priority %)) results))]
       (if (some #{(:aggregate/priority res)} aggs)
         ^{:key (str "card-row-" sid r)} [:tdh (or (:result/score res) [:code]) [:sup (:result/vs res)]]
         ^{:key (str "card-row-" sid r)} [:td (or (:result/score res) [:code]) [:sup (:result/vs res)]])))])

(defn- grade-card [grouped-results pri aggs]
  (for [grade (keys grouped-results)]
    ^{:key (str "grade-card-" grade)}
    [:card
     [:h4 (str "Grade: " grade)]
     [:table
      [:thead
       [:tr
        [:th "Rank"]
        [:th "Name"]
        (for [r pri]
          (if (some #{r} aggs)
            ^{:key (str "grand-agg" r)} [:thh "#" r]
            ^{:key (str "grand-agg" r)} [:th "#" r]))]]
      [:tbody
       (for [s (get grouped-results grade)]
         ^{:key (:shooter/sid s)} [agg-row pri aggs s])]]]))

(defn- page []
  (let [results @(rf/subscribe [:tv-results])
        aggregate-priorities (map :aggregate/priority @(rf/subscribe [:aggregates]))
        grouped-results (group-by :shooter/grade results)
        left-col (dissoc grouped-results "FS1" "FS2" "FO")
        right-col (dissoc grouped-results "A" "B" "C")
        f (first results)
        pri (apply sorted-set (into #{} (map :aggregate/priority (:aggregate/results f))))]
    [:section
     [:tv
      [:div {:local "1/2"} (grade-card left-col pri aggregate-priorities)]
      [:div {:local "1/2"} (grade-card right-col pri aggregate-priorities)]]]))

(secretary/defroute "/tv" []
  (rf/dispatch [:bull-clicked]))

(def pages
  {:tv #'page})
