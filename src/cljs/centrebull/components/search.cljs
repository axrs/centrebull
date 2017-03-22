(ns centrebull.components.search
  (:require
    [clojure.string :refer [trim]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [reagent.core :as reagent]))

(defn- results-table [results row-render]
  [:div
   (if-not (empty? @results)
     (if (nil? @results)
       [:p "No results found"]
       (for [r @results] (row-render r results))))])

(defn- set-search-watch [url ratom results]
  (let [timer (r/atom nil)]
    (add-watch ratom :auto-searcher
               (fn [_ s]
                 (let [query (:search @s)]
                   (when @timer (js/clearTimeout @timer))
                   (if (not-empty query)
                     (reset! timer (js/setTimeout #(rf/dispatch [:search url {:q query} results]) 500))
                     (reset! results nil)))))))

(defn search
  "Renders the HTML to view the all the pools matching the the search"
  [url {:keys [header row footer]}]
  (let [results (r/atom nil)
        search (r/atom {})]
    (reagent/create-class
      {:component-did-mount #(set-search-watch url search results)
       :display-name        "search"
       :reagent-render      (fn []
                              [:div
                               [input {:ratom search :key :search :placeholder "Search" :required? false :autofocus true}]
                               (when header [header])
                               [results-table results row]
                               (when footer [footer])])})))
