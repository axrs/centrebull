(ns centrebull.display.views)



(defn base-table []
  [:table
     [:thead
      [:tr
       [:th "#"]
       [:th "name"]]]])


(defn grade-a []

  [:card
     [base-table]])
