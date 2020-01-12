(ns space-ui.ui-logic.user-intents
  (:require [commons.constants.keycodes :as kc]
            [commons.dom :as dom]
            [commons.constants.user-intents :as intents]))

(defn- interpret-user-intent [^js/String target-value react-synth-evt]
  (let [shift?           (.-shiftKey react-synth-evt)
        window-selection (js/window.getSelection)
        v-length         ^js/Number (.-length target-value)
        key-code         (.-keyCode react-synth-evt)
        key*             (.-key react-synth-evt)
        cursor-position  (.-focusOffset window-selection)]
    (case (kc/kc->kw key-code)
      ::kc/enter        ::intents/create
      ::kc/escape       ::intents/escape
      ::kc/comma        (if (= key* ",") ::intents/comma)
      ::kc/arrow-down   ::intents/focus-down
      ::kc/arrow-up     ::intents/focus-up
      ::kc/arrow-left   (if (= 0 cursor-position) ::intents/focus-prev)
      ::kc/arrow-right  (if (= v-length cursor-position) ::intents/focus-next)
      ::kc/semicolon    (case key*
                          ":" ::intents/colon
                          ";" ::intents/semicolon
                          nil)
      ::kc/tab          (if shift?
                          ::intents/shift-tab
                          ::intents/tab)
      ::kc/backspace    (if (= 0 v-length)
                          ::intents/delete)
      nil)))

(defn key-down-evt->intent-evt
  "Parses react keydown evt and constructs an intent evt if intent is recognised.
   @returns map/nil

   boolean altKey
   number charCode
   boolean ctrlKey
   boolean getModifierState(key)
   string key
   number keyCode
   string locale
   number location
   boolean metaKey
   boolean repeat
   boolean shiftKey
   number which"
  [react-evt]
  (let [t      (.-target react-evt)
        dataset (aget t "dataset")
        v      (cond
                 (#{"INPUT" "TEXTAREA"} (.-tagName t)) (.-value t)
                 (aget dataset "textMode") (.-textContent t)
                 :else (.-innerHTML t))
        intent (interpret-user-intent v react-evt)]
    (when intent
      {:intent intent
       :value  v
       :target t
       :entity-id (dom/evt->entity-id react-evt)
       :data   (some-> dataset dom/dataset->clj-raw)})))

(defn handle-intent-with-intents-map [intents e]
  (let [evt (some-> e key-down-evt->intent-evt)]
    (when-let [intent-handler (get intents (:intent evt))]
      (.preventDefault e)
      (intent-handler evt))))
