(ns centrebull.ranges.core
  (:require
    [secretary.core :as secretary]
    [centrebull.components.modal :as modal]
    [centrebull.ranges.handlers]
    [centrebull.ranges.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
  (let [new-range (r/atom {})
        toggle-action #(modal/toggle new-range)
        reset-action #(modal/reset new-range)
        submit-action #(rf/dispatch [:ranges-create @new-range reset-action])
        valid? (fn [] (s/valid? :api/ranges-create @new-range))]
    (fn []
      [:div
       [v/ranges-page toggle-action]
       [modal/modal {:state new-range
                     :title "Register New Competition"
                     :view  [v/register submit-action valid? new-range]}]])))

(secretary/defroute "/ranges" []
  (rf/dispatch [:set-active-page :ranges]))

(def pages
  {:ranges #'page})
