(ns centrebull.competitions.core
  (:require
    [secretary.core :as secretary]
    [centrebull.components.modal :as modal]
    [centrebull.competitions.handlers]
    [centrebull.competitions.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (let [new-competition (r/atom {})
        toggle-action #(modal/toggle new-competition)
        reset-action #(modal/reset new-competition)
        submit-action #(rf/dispatch [:competitions-create @new-competition reset-action])
        valid? (fn [] (s/valid? :api/competition-create @new-competition))]
    (fn []
      [:div
       [v/competitions-page toggle-action]
       [modal/modal {:state new-competition
                     :title "Register New Competition"
                     :view  [v/register submit-action valid? new-competition]}]])))

(secretary/defroute "/competitions" []
  (rf/dispatch [:set-active-page :competitions]))

(def pages
  {:competitions #'page})
