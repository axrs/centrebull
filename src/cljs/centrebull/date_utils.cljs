(ns centrebull.date-utils
  (:require
    [goog.i18n.DateTimeFormat :as dtf]))

(def format-map
  (let [f goog.i18n.DateTimeFormat.Format]
    {:FULL_DATE       (.-FULL_DATE f)
     :FULL_DATETIME   (.-FULL_DATETIME f)
     :FULL_TIME       (.-FULL_TIME f)
     :LONG_DATE       (.-LONG_DATE f)
     :LONG_DATETIME   (.-LONG_DATETIME f)
     :LONG_TIME       (.-LONG_TIME f)
     :MEDIUM_DATE     (.-MEDIUM_DATE f)
     :MEDIUM_DATETIME (.-MEDIUM_DATETIME f)
     :MEDIUM_TIME     (.-MEDIUM_TIME f)
     :SHORT_DATE      (.-SHORT_DATE f)
     :SHORT_DATETIME  (.-SHORT_DATETIME f)
     :SHORT_TIME      (.-SHORT_TIME f)}))

(defn format-date-generic
  "Format a date using either the built-in goog.i18n.DateTimeFormat.Format enum
or a formatting string like \"dd MMMM yyyy\""
  [date-format date]
  (.format (goog.i18n.DateTimeFormat.
             (or (date-format format-map) date-format))
    (js/Date. date)))

(defn format-date [date]
  (format-date-generic :LONG_DATE date))
