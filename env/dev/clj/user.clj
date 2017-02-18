(ns user
  (:require [mount.core :as mount]
            [centrebull.figwheel :refer [start-fw stop-fw cljs]]
            centrebull.core))

(defn start []
  (mount/start-without #'centrebull.core/http-server
                       #'centrebull.core/repl-server))

(defn stop []
  (mount/stop-except #'centrebull.core/http-server
                     #'centrebull.core/repl-server))

(defn restart []
  (stop)
  (start))


