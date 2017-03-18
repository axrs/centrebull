(ns centrebull.shooters.core
  (:require [centrebull.shooters.view :as v]
            [secretary.core :as secretary]
            [re-frame.core :as rf]))

(defn- page []
  (fn []
    [:div
     (v/shooters-page)]))

(secretary/defroute "/shooters" []
  (rf/dispatch [:set-active-page :shooters]))

(def pages
  {:shooters #'page})
