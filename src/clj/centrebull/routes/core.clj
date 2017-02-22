(ns centrebull.routes.core
  (:require [centrebull.layout :as layout]
            [compojure.core :refer [defroutes context GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [centrebull.shooters.core :as shooters]))

(defn home-page []
  (layout/render "home.html"))

(defroutes routes
           (GET "/" [] (home-page))

           (context "/shooters" []
             (POST "/" {:as request} (shooters/create! request))
             (GET "/search" {:as request} (shooters/suggest request))
             (GET "/:id" {:as request} (shooters/find-by-id request))))
