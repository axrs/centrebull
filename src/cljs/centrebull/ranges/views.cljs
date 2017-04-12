 (ns centrebull.ranges.views
  (:require
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [re-frame.core :as rf]))

(defn- ranges-header [results]
       (when @results
             [:header
              [:caption "Description"]]))

(defn- ranges-row [{:keys [range/id range/description]}]
       [:div
        [:p description]])

(defn competitions-page [toggle-action]
      [:section
       [:card
        [:h2 {:local "9/12"} "Ranges"]
        [:button {:local "3/12" :on-click toggle-action} "New Range"]

        [search "/ranges/search"
         {:header ranges-header
          :row ranges-row}]]])

(defn register-modal [state valid? toggle-action submit-action]
      [:modal {:on-click toggle-action}
       [:card {:on-click #(.stopPropagation %)}
        [:h2 "Register New Competition"]
        [:grid
         [input {:title       "Description"
                 :grid        "1/1"
                 :ratom       state
                 :key         :range/description
                 :placeholder "Description"
                 :required?   true}]]
        [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit-action :disabled (not (valid?))} "Save"]]])
