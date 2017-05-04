(ns centrebull.components.modal
  (:require
    [re-frame.core :as rf]
    [reagent.core :as r]))

(def ^:private modal-key :modal-visible?)

(defn reset [state]
  (if (instance? reagent.ratom/RAtom state)
    (reset! state {})))

(defn toggle [state]
  (if (instance? reagent.ratom/RAtom state)
    (swap! state assoc modal-key (not (get @state modal-key)))))

(defn- render [title state view]
  (when (get @state modal-key)
    [:modal {:on-click #(toggle state)}
     [:card {:on-click #(.stopPropagation %)}
      (when title [:h2 title])
      view]]))

(defn modal
  "Displays a modal"
  [{:keys [title state view]
    :or   {state (r/atom {})}}]
  (r/create-class
    {:display-name   "Modal"
     :reagent-render (fn [m]
                       [render title state view])}))

