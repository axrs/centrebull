(ns centrebull.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [secretary.core :as secretary]
            [accountant.core :as accountant]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [centrebull.spec]
            [centrebull.ajax :refer [load-interceptors!]]
            [centrebull.handlers]
            [centrebull.components.navigation :refer [topbar sidebar]]
            [centrebull.subscriptions]
            [centrebull.competitions.core :as competitions]
            [centrebull.shooters.core :as shooters])
  (:import goog.History))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn home-page []
  [:div.container])

(def base-pages
  {:home #'home-page
   :about #'about-page})

(defn pages []
  (-> {}
    (merge
      base-pages
      competitions/pages
      shooters/pages)))

(defn page []
  [:div
   [topbar]
   [sidebar]
   [:page
    [((pages) @(rf/subscribe [:page]))]]])


;; -------------------------
;; Routes
(accountant/configure-navigation! {:nav-handler  (fn [path] (secretary/dispatch! path))
                                   :path-exists? (fn [path] (secretary/locate-route path))})

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))


;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (accountant/navigate! (str "#" (.-token event)))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn on-window-resize [evt]
  (rf/dispatch [:set-page-width (.-innerWidth js/window)]))


(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app"))
  (.addEventListener js/window "resize" on-window-resize)
  (rf/dispatch [:set-page-width (.-innerWidth js/window)]))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
