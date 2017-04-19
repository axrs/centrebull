(ns centrebull.activities.core
  (:require
    [secretary.core :as secretary]
    [centrebull.activities.handlers]
    [centrebull.activities.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (fn []
    [:div]))

(secretary/defroute "/activities" []
  (rf/dispatch [:activities-load]))

(def pages
  {:activities #'page})
