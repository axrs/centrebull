(ns centrebull.components.navigation
  (:require
    [accountant.core :as accountant]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [secretary.core :as secretary]
    [goog.events :as events]
    [ajax.core :refer [GET POST]]))

(defn sidebar-link [action title page]
  (let [selected-page (rf/subscribe [:page])]
    [:li (when (= page @selected-page) {:fx "active"})
     [:a {:on-click action} title]]))

(defn- sidebar []
  (let [is-open? (rf/subscribe [:sidebar-open?])
        is-forced? (rf/subscribe [:force-sidebar-open?])]
    [:sidebar
     [:ul {:style {:transform (when (and (not @is-open?) (not @is-forced?)) "translate3d(-100%,0,0)")}}
      [sidebar-link #(accountant/navigate! "#/") "Home" :home]
      [sidebar-link #(accountant/navigate! "#/competitions") "Competitions" :competitions]]]))

(defn topbar []
  [:nav
   {:fx ""}
   [:label
    (let [is-open? (rf/subscribe [:sidebar-open?])]
      [:input {:type "checkbox" :value @is-open? :on-change #(rf/dispatch [:toggle-sidebar])}])
    [:header
     [:a {:on-click #(accountant/navigate! "#/")} "Centre" [:strong "Bull"]]]]])
