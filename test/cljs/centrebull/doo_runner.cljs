(ns centrebull.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [centrebull.core-test]))

(doo-tests 'centrebull.core-test)

