(ns centrebull.activities.views
  (:require
    [centrebull.components.autocomplete :refer [autocomplete]]
    [centrebull.components.search :refer [search]]
    [centrebull.components.input :refer [input]]
    [centrebull.date-utils :refer [format-date]]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [cljs.spec :as s]
    [clojure.string :as string]))

(defn activity-row
  [{:keys [activity/date
           range/description
           activity/priority]}]
  [:tr
   [:td priority]
   [:td (format-date date)]
   [:td description]])

(defn activites-page [toggle-action activities]
  [:section
   [:card
    [:h2 {:local "9/12"} "Activities"]
    [:button {:local "3/12" :on-click toggle-action} "New Activity"]
    [:table
     [:thead
      [:tr
       [:th "Priority"]
       [:th "Date"]
       [:th "Description"]]]
     [:tbody
      (for [act activities]
        ^{:key (:activity/id act)} [activity-row act])]]]])

(defn register [submit valid? state]
  [:div
   [:grid
    [autocomplete "/ranges/search" {:title "Range" :k :range/description :on-select #(swap! state assoc :activity/range-id (:range/id %))}]
    [input {:title       "Date"
            :grid        "1/2"
            :ratom       state
            :key         :activity/date
            :placeholder "YYYY-MM-DD"
            :required?   true}]
    [input {:title       "Priority"
            :grid        "1/2"
            :ratom       state
            :key         :activity/priority
            :placeholder "0"
            :required?   true}]]
   [:button {:data-pull-left "9/12"
             :local          "3/12"
             :data-m-full    ""
             :data-primary   ""
             :on-click       submit
             :disabled       (not (valid?))}
    "Save"]])

(defn clean-shots [{:keys [result/shots] :as state}]
  (if (empty? shots)
    state
    (assoc state :result/shots
                 (-> shots
                   (clojure.string/replace #"-| " "")
                   clojure.string/upper-case))))


(defn register-result [submit valid? state]
  (let [valid? (valid?)
        score (when valid? (s/conform :api/result-create (clean-shots @state)))]
    [:div
     [:h3 (:shooter/name @state)]
     [:grid
      [input {:title       "Shots"
              :ratom       state
              :grid        "2/3"
              :key         :result/shots
              :required?   true
              :auto-focus? true
              :placeholder "shots"
              :submit      submit}]
      [:h3 {:local "1/3"} (:result/score score) [:sup (:result/vs score)]]
      [:button {:data-pull-left "9/12" :local "3/12" :data-m-full "" :data-primary "" :on-click submit :disabled (not valid?)} "Save"]]]))

(defn row-render [toggle {:keys [:rank :shooter/first-name :shooter/last-name :shooter/sid :result/shots :result/vs :result/score :shooter/grade]}]
  (let [n (str first-name " " last-name)]
    [:tr
     [:td grade rank]
     [:td n]
     [:td {:style {:text-align "right"}} shots]
     [:tdh {:style {:text-align "right"}} score [:sup vs]]
     [:td {:style {:text-align "right"}}
      (when @(rf/subscribe [:admin?]) [:button {:type "button" :on-click #(toggle sid n)} "Edit"])]]))

(defn shooter-match [search r]
  (let [n (string/join " " (map (comp string/lower-case str) (filter some? (vals r))))
        search (string/split (string/lower-case search) " ")]
    (not-empty (filter seq (map #(re-find (re-pattern %) n) search)))))

(defn generate-table [search toogle results]
  [:table
   [:thead
    [:tr [:th {:col-span 7} [input {:key :search :placeholder "Type to Filter or Search" :ratom search :list "results-autocomplete" :auto-focus? true}]]]
    [:tr
     [:th ""]
     [:th "Name"]
     [:th {:style {:text-align "right"}} "Shots"]
     [:thh {:style {:text-align "right"}} "Score"]
     [:th]]]
   [:tbody
    (let [filtered (if (not-empty (:search @search))
                     (filter #(shooter-match (:search @search) %) results)
                     results)]
      (for [r filtered]
        ^{:key (:shooter/sid r)} [row-render toogle r]))]])


(defn single-activity-page [toggle {:keys [range/description activity/priority activity/date]} results]
  (let [search (r/atom {:search ""})]
    (fn []
      [:section
       [:card
        [:h2 description [:sub "#" priority " " (format-date date)]]
        [:h3 "Shooters"]]
       [:card
        [generate-table search toggle @results]]])))
