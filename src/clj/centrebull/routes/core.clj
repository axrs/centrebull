(ns centrebull.routes.core
  (:require [centrebull.layout :as layout]
            [compojure.api.sweet :refer [defapi context GET POST PUT DELETE]]
            [centrebull.routes.specify]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [centrebull.shooters.core :as shooters]
            [centrebull.activities.core :as activities]
            [centrebull.ranges.core :as ranges]
            [centrebull.results.core :as results]
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

    (GET "/search" {:as request}
      :spec :api/search
      (shooters/suggest request))

    (POST "/search" {:as request}
      :spec :api/search
      (shooters/suggest request))

    (GET "/:shooter--sid" {:as request}
      :spec :api/shooter-id-only
      (shooters/find-by-id request)))

  (context "/competitions" []
    (POST "/" {:as request}
      :spec :api/competition-create
      (competitions/create! request))

    (POST "/search" {:as request}
      :spec :api/search
      (competitions/suggest request))

    (context "/:competition--id" []
      (GET "/" {:as request}
        :spec :api/competition-id-only
        (competitions/find request))

      (DELETE "/" {:as request}
        :spec :api/competition-id-only
        (competitions/delete! request))

      (GET "/activities" {:as request}
        :spec :api/competition-id-only
        (competitions/find-activities request))

      (GET "/registrations" {:as request}
        :spec :api/competition-and-activity-id-only
        (registrations/retrieve-registrations request))))

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

  (context "/results" []
    (POST "/" {:as request}
      :spec :api/result-create
      (results/create! request)))

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
      :spec :api/search
      (ranges/suggest request))

    (DELETE "/:range--id" {:as request}
      :spec :api/range-id-only
      (ranges/delete! request))))

