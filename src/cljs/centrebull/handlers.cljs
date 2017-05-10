(ns centrebull.handlers
  (:require [centrebull.db :as db]
            [centrebull.ajax :refer [post-json]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [accountant.core :as accountant]
            [clojure.string :as str]))

(reg-event-db
  :initialize-db
  (fn [_ _] db/default-db))

(reg-event-fx
  :set-active-page
  (fn [{:keys [db]} [_ page]]
    {:db (assoc db :page page)}))

(defn- prefix-url [u]
  (if (str/starts-with? u "#") u (str "#" u)))

(reg-event-fx
  :set-page-url
  (fn [_ [_ url]] (accountant/navigate! (prefix-url url))))

(reg-event-db
  :toggle-sidebar
  (fn [db [_]]
    (assoc db :sidebar-open? (not (:sidebar-open? db)))))

(reg-event-db
  :try-force-sidebar-open
  (fn [db [_ value]]
    (assoc db :force-sidebar-open? (> value 800))))

(reg-event-fx
  :set-page-width
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc db :page-width value)
     :dispatch [:try-force-sidebar-open value]}))

(reg-event-fx ::update-results-atom
  (fn [_ [_ ratom results]]
    (reset! ratom results)
    {}))

(reg-event-fx
  :search
  (fn [_ [_ url params ratom]]
    (post-json {:url           url
                :body          params
                :after-success [[::update-results-atom ratom]]})))

(defn- reset-atom [a]
  (if (instance? reagent.ratom/RAtom a)
    (reset! a {})))

(reg-event-fx
  :toggle
  (fn [_ [_ ratom & others]]
    (reset! ratom (not @ratom))
    (doall (map reset-atom others))
    {}))

(reg-event-fx
  :select-autocomplete-text
  (fn [_ [_ id]]
    (.select (.getElementById js/document id))))
