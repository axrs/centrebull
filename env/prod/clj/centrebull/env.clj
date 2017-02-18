(ns centrebull.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[centrebull started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[centrebull has shut down successfully]=-"))
   :middleware identity})
