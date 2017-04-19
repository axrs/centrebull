(ns centrebull.routes.core
  (:require [centrebull.layout :as layout]
            [compojure.api.sweet :refer [defapi context GET POST PUT DELETE]]
            [centrebull.routes.specify]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [centrebull.shooters.core :as shooters]
            [centrebull.activities.core :as activities]
            [centrebull.ranges.core :as ranges]
            [centrebull.competitions.core :as competitions]
            [centrebull.registrations.core :as registrations]))

(defn home-page []
  (layout/render "home.html"))

(defapi routes
  (GET "/" [] (home-page))

  (context "/shooters" []
    (POST "/" {:as request}
      :spec :api/shooter-create
      (shooters/create! request))

    (GET "/search" {:as request} (shooters/suggest request))

    (POST "/search" {:as request} (shooters/suggest request))

    (GET "/:shooter--sid" {:as request}
      :spec :api/shooter-id-only
      (shooters/find-by-id request)))

  (context "/competitions" []
    (POST "/" {:as request}
      :spec :api/competition-create
      (competitions/create! request))

    (POST "/search" {:as request}
      :spec :api/competition-suggest
      (competitions/suggest request))

    (GET "/:competition--id" {:as request}
      :spec :api/competition-id-only
      (competitions/find request))

    (DELETE "/:competition--id" {:as request}
      :spec :api/competition-id-only
      (competitions/delete! request))

    (GET "/:competition--id/activities" {:as request}
      :spec :api/competition-id-only
      (competitions/find-activities request)))

  (context "/registrations" []
    (DELETE "/:entry--id" {:as request}
      :spec :api/competition-unregister-shooter
      (registrations/unregister-shooter! request))

    (POST "/" {:as request}
      :spec :api/competition-register-shooter
      (registrations/register-shooter! request))

    (POST "/search" {:as request}
      :spec :api/competition-suggest-registration
      (registrations/suggest-registration request)))

  (context "/activities" []
    (POST "/" {:as request}
      :spec :api/activity-create
      (activities/create! request))

    (DELETE "/:activity--id" {:as request}
      :spec :api/activity-id-only
      (activities/delete! request)))

  (context "/ranges" []
    (POST "/" {:as request}
      :spec :api/ranges-create
      (ranges/create! request))

    (POST "/search" {:as request}
      :spec :api/ranges-suggest
      (ranges/suggest request))

    (DELETE "/:range--id" {:as request}
      :spec :api/range-id-only
      (ranges/delete! request))))

