(ns centrebull.test.wrapper
  (:require [mount.core :as mount]))

(defn wrap-test [f]
  (mount/start #'centrebull.config/env)
  (f)
  (mount/stop))
