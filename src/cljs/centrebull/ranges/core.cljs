(ns centrebull.ranges.core
  (:require
    [secretary.core :as secretary]
    [centrebull.ranges.handlers]
    [centrebull.ranges.views :as v]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [clojure.spec :as s]))

(defn- page []
       (let [new-range (r/atom {})
             show-modal? (r/atom false)
             toggle-action #(rf/dispatch [:toggle show-modal? new-range])
             submit-action #(rf/dispatch [:ranges-create @new-range [[:toggle show-modal? new-range]]])
             valid? (fn [] (s/valid? :api/ranges-create @new-range))]
            (fn []
                [:div
                 (v/ranges-page toggle-action)
                 (when @show-modal?
                       (v/register-modal new-range valid? toggle-action submit-action))])))

(secretary/defroute "/ranges" []
                    (rf/dispatch [:set-active-page :ranges]))

(def pages
  {:ranges #'page})
