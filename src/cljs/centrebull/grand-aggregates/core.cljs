(ns centrebull.grand-aggregates.core
  (:require [secretary.core :as secretary]
            [re-frame.core :as rf]))

(defn- page []
  [:h1 "grand aggregates"])

(def pages
  {:grand-aggregates #'page})

(secretary/defroute "/grand-aggregates" []
  (rf/dispatch [:set-active-page :grand-aggregates]))
