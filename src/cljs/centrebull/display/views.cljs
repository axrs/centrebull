(ns centrebull.display.views)

(defn grade-limit [grade]
  (case grade
    "A"   10
    "B"   8
    "C"   5
    "FO"  5
    "FTR" 5
    "FSA" 6
    "FSB" 6))
  

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
    [:tbody
     [:tr [:td {:col-span "50"} "Grade: " grade]]
     (let [results (get grouped-results grade)
           limited-results (take (grade-limit grade) results)]
      (for [s limited-results]
        ^{:key (:shooter/sid s)} [agg-row pri aggs s]))]))

(defn display-page [results aggregate-priorities grouped-results right-col left-col pri]
  [:section
     [:tv
      [:div {:local "1/2"}
       [:card
        [:table
         [:thead
          [:tr
           [:th "#"]
           [:th "Name"]
           (for [r pri]
             (if (some #{r} aggregate-priorities)
               ^{:key (str "grand-agg" r)} [:thh "#" r]
               ^{:key (str "grand-agg" r)} [:th "#" r]))]]
         (grade-card right-col pri aggregate-priorities)]]]
      [:div {:local "1/2"}
       [:card
        [:table
         [:thead
          [:tr
           [:th "#"]
           [:th "Name"]
           (for [r pri]
             (if (some #{r} aggregate-priorities)
               ^{:key (str "grand-agg" r)} [:thh "#" r]
               ^{:key (str "grand-agg" r)} [:th "#" r]))]]
         (grade-card left-col pri aggregate-priorities)]]]]])
