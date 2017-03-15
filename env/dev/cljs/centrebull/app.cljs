(ns ^:figwheel-no-load centrebull.app
  (:require [centrebull.core :as core]
            [devtools.core :as devtools]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://10.143.50.121:3449/figwheel-ws"
  :on-jsload core/mount-components)

(devtools/install!)

(core/init!)
