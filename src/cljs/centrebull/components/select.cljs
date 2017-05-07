(ns centrebull.components.select)

(defn target-value [event] (.-target.value event))

(defn filter-values
  [pred map]
  (into {} (filter (comp pred val) map)))

(def filter-empty (partial filter-values #(if (or (coll? %) (string? %))
                                            (seq %)
                                            (some? %))))

(defn- render-option [{:keys [id name label selected? disabled?]}]
  (let [attr (filter-empty {:value    id
                            :label    label
                            :selected selected?
                            :disabled disabled?})]
    ^{:key id} [:option attr name]))

(defn select
  "Form-2 component for creating <select>s"
  ([f]
   (select f {}))
  ([f m]
   (let [p (merge m {:on-change (comp f target-value)})]
     (fn [list]
       (apply conj [:select p] (map render-option list))))))
