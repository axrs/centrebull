(ns centrebull.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [centrebull.layout :refer [error-page]]
            [centrebull.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [centrebull.env :refer [defaults]]
            [mount.core :as mount]
            [centrebull.middleware :as middleware]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
      (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title  "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
