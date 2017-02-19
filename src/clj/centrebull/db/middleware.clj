(ns centrebull.db.middleware
  (:require
    [conman.core :refer [with-transaction]]
    [centrebull.config :refer [env]]
    [centrebull.db.core :as db]))

(defn wrap-transactional
  "Wraps incoming requests to operate within a database transaction."
  [handler]
  (fn [request]
    (let [wrap? (env :wrap-transaction)]
      (if wrap?
        (with-transaction [db/*db*]
                          (handler request))
        (handler request)))))
