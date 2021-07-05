(ns space-ui.ui-logic.user-intents
  (:require [commons.constants.keycodes :as kc]
            [space-ui.util.dom :as dom]))


(defn react-evt->keycode-kw [^js react-synth-evt]
  (some-> react-synth-evt (.-keyCode) kc/kc->kw))


(defn- interpret-user-intent
  [^js/String target-value ^js react-synth-evt]
  (let [shift?           (.-shiftKey react-synth-evt)
        window-selection (js/window.getSelection)
        v-length         ^js/Number (.-length target-value)
        key-code-kw      (react-evt->keycode-kw react-synth-evt)
        key*             (.-key react-synth-evt)
        cursor-position  (.-focusOffset window-selection)]
    (case key-code-kw
      ::kc/n            :intents/create
      ::kc/enter        :intents/commit
      ::kc/escape       :intents/escape
      ::kc/comma        (if (= key* ",") :intents/comma)
      ::kc/arrow-down   :intents/focus-down
      ::kc/arrow-up     :intents/focus-up
      ::kc/arrow-left   (if (= 0 cursor-position) :intents/focus-prev)
      ::kc/arrow-right  (if (= v-length cursor-position) :intents/focus-next)
      ::kc/semicolon    (case key*
                          ":" :intents/colon
                          ";" :intents/semicolon
                          nil)
      ::kc/tab          (if shift?
                          :intents/shift-tab
                          :intents/tab)
      ::kc/backspace    (if (= 0 v-length)
                          :intents/delete)
      nil)))


(defn assert-intents2 [intents]
  (when intents
    (assert (map? intents) "must be a map")
    (when (not-empty intents)
      (assert (= "intents" (namespace (ffirst intents))) "intents keys be in the 'intents namespace"))))

(assert-intents2 nil)
(assert-intents2 {})
(assert-intents2 {:intents/create 1})


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
  [^js react-evt]
  (let [t (.-target react-evt)
        dataset (aget t "dataset")
        keycode-kw (react-evt->keycode-kw react-evt)
        v (cond
            (#{"INPUT" "TEXTAREA"} (.-tagName t)) (.-value t)
            (aget dataset "textMode") (.-textContent t)
            :else (.-innerHTML t))
        intent (interpret-user-intent v react-evt)]
    (when intent
      {:evt/intent     intent
       :evt/keycode-kw keycode-kw
       :evt/value      v
       :evt/target     t
       :evt/entity-id  (dom/evt->entity-id react-evt)
       :evt/data       (some-> dataset dom/dataset->clj-raw)})))


(defn handle-intent-with-intents-map [intents react-key-evt]
  (let [evt (some-> react-key-evt key-down-evt->intent-evt)]
    (when-let [intent-handler (get intents (:evt/intent evt))]
      (.preventDefault react-key-evt)
      (intent-handler evt))))
