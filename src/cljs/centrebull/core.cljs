(ns centrebull.core
  (:require
    [reagent.core :as r]
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
    [centrebull.activities.core :as activities]
    [centrebull.aggregates.core :as aggregates]
    [centrebull.grand-aggregates.core :as grand-aggregates]
    [centrebull.shooters.core :as shooters]
    [centrebull.ranges.core :as ranges]
    [centrebull.display.core :as tv])


  (:import goog.History))

(def default-route "#/competition/")

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn home-page []
  [:div.container])

(def base-pages
  {:home  #'home-page
   :about #'about-page})

(defn pages []
  (-> {}
    (merge
      base-pages
      ranges/pages
      competitions/pages
      activities/pages
      aggregates/pages
      shooters/pages
      tv/pages
      grand-aggregates/pages)))

(defn page []
  (let [page @(rf/subscribe [:page])]
    [:div {:id (str (name page) "-page")}
     [topbar]
     [sidebar]
     [:page
      [((pages) page)]]]))


;; -------------------------
;; Routes
(accountant/configure-navigation! {:nav-handler  (fn [path] (secretary/dispatch! path))
                                   :path-exists? (fn [path] (secretary/locate-route path))})

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/c967c421-16b8-428d-9984-2698ecac1a72" []
  (rf/dispatch [:set-admin? true]))

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
  (accountant/navigate! default-route)
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))  
