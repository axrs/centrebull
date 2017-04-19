(ns centrebull.components.autocomplete
  (:require
    [clojure.string :refer [trim]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [reagent.core :as reagent]))

(defn- after-mount [url ratom results]
  (let [timer (r/atom nil)]
    (add-watch ratom :auto-searcher
      (fn [_ s]
        (let [query (:search @s)]
          (when @timer (js/clearTimeout @timer))
          (if (not-empty query)
            (reset! timer (js/setTimeout #(rf/dispatch [:search url {:search/q query} results]) 500))
            (reset! results nil)))))))

(defn- set-selected [results active search selected k on-select]
  (swap! search assoc :search (k selected))
  (reset! results nil)
  (reset! active selected)
  (if on-select (on-select selected)))

(defn autocomplete
  [url {:keys [on-select k title]
        :or   {k :description}}]
  (let [results (r/atom nil)
        active (r/atom nil)
        search (r/atom {})]
    (reagent/create-class
      {:component-did-mount #(after-mount url search results)
       :display-name        "Autocomplete"
       :reagent-render      (fn []
                              [:div {:local "1/1"}
                               [input {:ratom       search
                                       :title       title
                                       :key         :search
                                       :placeholder "Search"
                                       :required?   true
                                       :autofocus   true}]
                               (let [filtered (filter #(not= @active %) @results)]
                                 (when (not-empty filtered)
                                   [:autocomplete
                                    (for [result filtered]
                                      [:li {:on-click #(set-selected results active search result k on-select)}
                                       (k result)])]))])})))




