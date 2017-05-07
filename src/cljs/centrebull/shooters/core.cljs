(ns centrebull.shooters.core
  (:require [centrebull.shooters.view :as v]
            [centrebull.components.modal :as modal]
            [secretary.core :as secretary]
            [centrebull.shooters.handlers]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cljs.spec :as s]))

(defn- page []
  (let [new-shooter (r/atom {})
        toggle-action #(modal/toggle new-shooter)
        reset-action #(modal/reset new-shooter)
        submit-action #(rf/dispatch [:shooters-create @new-shooter reset-action])
        valid? (fn [] (s/valid? :api/shooter-create @new-shooter))]
    (fn []
      [:div
       [v/shooters-page toggle-action]
       [modal/modal {:state new-shooter
                     :title "Register a New Shooter"
                     :view  [v/register submit-action valid? new-shooter]}]])))

(secretary/defroute "/shooters" []
  (rf/dispatch [:set-active-page :shooters]))

(def pages
  {:shooters #'page})
