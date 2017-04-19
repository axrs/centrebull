(ns centrebull.components.input)

(defn input
  "Creates a basic text input.
  ratom: Reagent atom associated with the input binding
  key: Key within the ratom
  type: The type of input [Optional, defaults to 'text']
  title: Label Title [Optional, defaults to '']
  placeholder: Input placeholder text [Optional, defaults to '']
  required?: Is the input field required? [Optional, defaults to true]
  submit: Action to run when 'enter' is pressed on the field [Optional]
  "
  [{:keys [key ratom grid placeholder required? type submit title list]
    :or   {grid "" placeholder "" required? true type "text" submit nil title nil list nil}}]
  [:div (when (not-empty grid) {:local grid})
   (when title [:label title])
   [:input {:type         type
            :required     required?
            :placeholder  placeholder
            :min          0
            :id           (when list (str "awesomplete-" list))
            :list         list
            :on-change    #(swap! ratom assoc key (.-target.value %))
            :value        (get-in @ratom [key])
            :on-key-press (when submit #(if (= 13 (.-charCode %)) (submit)))}]
   (when required? [:span])])
