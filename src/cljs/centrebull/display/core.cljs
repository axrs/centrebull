(ns centrebull.display.core
  (:require
    [secretary.core :as secretary]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [centrebull.display.views :as views]))


(defn- page []
  [:section
   [:tv
    [views/grade-a]
    [views/grade-a]
    [views/grade-a]
    [views/grade-a]

    [views/grade-a]
    [views/grade-a]]])
;[:h1 "Hello world"])

(secretary/defroute "/tv" []
  (rf/dispatch [:bull-clicked]))

(def pages
  {:tv #'page})
