(ns centrebull.competitions.core
  (:require
    [secretary.core :as secretary]
    [centrebull.competitions.handlers]
    [centrebull.competitions.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (let [new-competition (r/atom {})
        errors (r/atom {})
        show-modal? (r/atom false)
        toggle-action #(rf/dispatch [:toggle show-modal? new-competition errors])
        submit-action #(rf/dispatch [:competitions-create @new-competition errors [[:toggle show-modal? new-competition errors]]])
        valid? (fn [] (s/valid? :api/competition-create @new-competition))]
    (fn []
      [:div
       (v/competitions-page toggle-action)
       (when @show-modal?
         (v/register-modal new-competition valid? toggle-action submit-action))])))

(secretary/defroute "/competitions" []
                    (rf/dispatch [:set-active-page :competitions]))

(def pages
  {:competitions #'page})
