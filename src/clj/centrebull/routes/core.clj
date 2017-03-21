(ns centrebull.routes.core
  (:require [centrebull.layout :as layout]
            [compojure.api.sweet :refer [defapi context GET POST PUT DELETE]]
            [centrebull.routes.specify]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [centrebull.shooters.core :as shooters]
            [centrebull.activities.core :as activities]
            [centrebull.ranges.core :as ranges]
            [centrebull.competitions.core :as competitions]))

(defn home-page []
  (layout/render "home.html"))

(defapi routes
  (GET "/" [] (home-page))

  (context "/shooters" []
    (POST "/" {:as request}
      :spec :centrebull.spec/shooter-create
      (shooters/create! request))

    (GET "/search" {:as request} (shooters/suggest request))

    (POST "/search" {:as request} (shooters/suggest request))

    (GET "/:shooter--sid" {:as request}
         :spec :centrebull.spec/shooter-id-only
         (shooters/find-by-id request)))

  (context "/competitions" []
    (POST "/" {:as request}
      :spec :centrebull.spec/competition-create
      (competitions/create! request))

    (GET "/search" {:as request} (competitions/suggest request))

    (POST "/search" {:as request} (competitions/suggest request))

    (GET "/:competition--id" {:as request}
      :spec :centrebull.spec/competition-id-only
      (competitions/find request))

    (DELETE "/:competition--id" {:as request}
      :spec :centrebull.spec/competition-id-only
      (competitions/delete! request))

    (POST "/:competition--id/registrations" {:as request}
      :spec :centrebull.spec/competition-register-shooter
      (competitions/register-shooter! request))

    (POST "/:competition--id/registrations/search" {:as request}
      (competitions/suggest-registration request)))

  (context "/activities" []
    (POST "/" {:as request}
      :spec :centrebull.spec/activity-create
      (activities/create! request))

    (DELETE "/:activity--id" {:as request}
      :spec :centrebull.spec/activity-id-only
      (activities/delete! request)))

  (context "/ranges" []
    (POST "/" {:as request}
      :spec :centrebull.spec/ranges-create
      (ranges/create! request))

    (DELETE "/:range--id" {:as request}
      :spec :centrebull.spec/range-id-only
      (ranges/delete! request))))

