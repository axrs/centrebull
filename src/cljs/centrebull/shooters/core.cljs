(ns centrebull.shooters.core
  (:require [centrebull.shooters.view :as v]
            [secretary.core :as secretary]
            [centrebull.shooters.handlers]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cljs.spec :as s]))

(defn- page []
  (let [new-shooter (r/atom {})
        errors (r/atom {})
        show-model? (r/atom false)
        toggle-action #(rf/dispatch [:toggle show-model? new-shooter errors])
        submit-action #(rf/dispatch [:shooters-create @new-shooter errors [[:toggle show-model? new-shooter errors]]])
        valid? (fn [] (s/valid? :centrebull.spec/shooter-create @new-shooter))]

    (fn []
      [:div
       (v/shooters-page toggle-action)
       (when @show-model?
         (v/register-modal new-shooter valid? toggle-action submit-action))])))

(secretary/defroute "/shooters" []
  (rf/dispatch [:set-active-page :shooters]))

(def pages
  {:shooters #'page})
