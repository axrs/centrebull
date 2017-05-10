(ns centrebull.components.navigation
  (:require
    [accountant.core :as accountant]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [secretary.core :as secretary]
    [centrebull.date-utils :refer [format-date]]
    [goog.events :as events]
    [ajax.core :refer [GET POST]]))

(defn sidebar-link [action title page show?]
  (when show?
    (let [selected-page (rf/subscribe [:page])]
      [:li (when (= page @selected-page) {:fx "active"})
       [:a {:on-click action} title]])))

(defn activity-link [{:keys [range/description activity/id activity/priority activity/date] :as r}]
  ^{:key id}[:li
               [:a {:on-click #(accountant/navigate! (str "#/activities/" id))} priority ": " description]])

(defn aggregate-link [{:keys [aggregate/description aggregate/id aggregate/priority] :as r}]
  ^{:key id}[:li
              [:a {:on-click #(accountant/navigate! (str "#/aggregates/" id))} priority ": " [:strong description]]])

(defn activity-section []
  (let [all-activities @(rf/subscribe [:aggregates-and-activities])]
    [:li
     [:label
      [:ul
       (map #(if (:activity/id %) (activity-link %) (aggregate-link %)) all-activities)]]]))

(defn- sidebar []
  (let [is-open? (rf/subscribe [:sidebar-open?])
        competiton-id (rf/subscribe [:active-competition-id])
        is-forced? (rf/subscribe [:force-sidebar-open?])]
    [:sidebar
     [:ul {:style {:transform (when (and (not @is-open?) (not @is-forced?)) "translate3d(-100%,0,0)")}}
      [sidebar-link #(accountant/navigate! "#/shooters") "Shooters" :shooters @competiton-id]
      [sidebar-link #(accountant/navigate! "#/competitions") "Competitions" :competitions (not @competiton-id)]
      [sidebar-link #(accountant/navigate! "#/ranges") "Ranges" :ranges true]
      [sidebar-link #(accountant/navigate! "#/activities") "New activity" :activities @competiton-id]
      [sidebar-link #(accountant/navigate! "#/aggregates") "Aggregates" :aggregate @competiton-id]
      [sidebar-link #(accountant/navigate! "#/grand-aggregates") "Grand Aggregates" :grand-aggregate @competiton-id]
      (activity-section)]]))

(defn topbar []
  [:nav
   {:fx ""}
   [:label
    (let [is-open? (rf/subscribe [:sidebar-open?])]
      [:input {:type "checkbox" :value @is-open? :on-change #(rf/dispatch [:toggle-sidebar])}])
    [:header
     (let [comp-desc (:competition/description @(rf/subscribe [:active-competition]))]
       [:a {:on-click #(accountant/navigate! "#/")} [:img {:src "/favicon.ico"}] "Centre" [:strong "Bull"]
        (when comp-desc [:span {:style {:font-weight 100}} (str " - " comp-desc)])])]]])

