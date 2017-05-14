(ns centrebull.display.core
  (:require
    [secretary.core :as secretary]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [centrebull.display.views :as views]))

(defn agg-row [pri {:keys [shooter/grade shooter/first-name shooter/last-name shooter/club shooter/sid aggregate/results aggregate/score aggregate/vs]}]
  [:tr
   [:td first-name " " last-name]
   (for [r pri]
     ^{:key (str sid r)} (let [res (first (filter #(= r (:aggregate/priority %)) results))]
                           [:td (or (:result/score res) [:code]) [:sup (:result/vs res)]]))])

(defn- grade-card [grouped-results pri]
  (for [grade (keys grouped-results)]
    [:card
     [:h4 (str "Grade: " grade)]
     [:table
      [:thead
       [:tr]
       [:th "Name"]
       (for [r pri]
         ^{:key (str "grand-agg" r)} [:th "#" r])]
      [:tbody
       (for [s (get grouped-results grade)]
         ^{:key (:shooter/sid s)} [agg-row pri s])]]]))

(defn- page []
  (let [results @(rf/subscribe [:tv-results])
        grouped-results (group-by :shooter/grade results)
        left-col (dissoc grouped-results "FS1" "FS2" "FO")
        right-col (dissoc grouped-results "A" "B" "C")
        f (first results)
        pri (apply sorted-set (into #{} (map :aggregate/priority (:aggregate/results f))))]
    [:section
     [:tv
      [:ul (grade-card left-col pri)]
      [:ul (grade-card right-col pri)]]]))

(secretary/defroute "/tv" []
  (rf/dispatch [:bull-clicked]))

(def pages
  {:tv #'page})
