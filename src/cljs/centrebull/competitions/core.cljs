(ns centrebull.competitions.core
  (:require
    [secretary.core :as secretary]
    [centrebull.competitions.views :as v]
    [re-frame.core :as rf]))

(defn- page []
  (v/competitions-page))

(secretary/defroute "/competitions" []
  (rf/dispatch [:set-active-page :competitions]))

(def pages
  {:competitions #'page})
