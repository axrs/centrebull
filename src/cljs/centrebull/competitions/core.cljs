(ns centrebull.competitions.core
  (:require
    [secretary.core :as secretary]
    [centrebull.competitions.handlers]
    [clojure.spec :as s]
    [centrebull.competitions.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]))

(defn- page []
  (let [new-competition (r/atom {})
        errors (r/atom {})
        show-modal? (r/atom false)
        toggle-action #(rf/dispatch [:toggle show-modal? new-competition errors])
        submit-action #(rf/dispatch [:competitions-create @new-competition errors [[:toggle show-modal? new-competition errors]]])
        valid? (fn [] (s/valid? :centrebull.spec/competition-create @new-competition))]
    (fn []
      [:div
       (v/competitions-page toggle-action)
       (when @show-modal?
         (v/register-modal new-competition valid? toggle-action submit-action))])))

(secretary/defroute "/competitions" []
                    (rf/dispatch [:set-active-page :competitions]))

(def pages
  {:competitions #'page})
