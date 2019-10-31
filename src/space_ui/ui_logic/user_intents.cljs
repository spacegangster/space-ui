(ns space-ui.ui-logic.user-intents
  (:require [commons.constants.keycodes :as kc]
            [commons.dom :as dom]
            [commons.constants.user-intents :as intents]))


(defn interpret-user-intent [target-value key-code]
  (if (empty? target-value)
    (case (kc/kc->kw key-code)
      ::kc/enter               ::intents/create
      ::kc/backspace-or-delete ::intents/delete
      nil)
    (case (kc/kc->kw key-code)
      ::kc/enter               ::intents/create
      ::kc/backspace-or-delete nil
      nil)))

(defn key-down-evt->intent-evt
  "boolean altKey
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
        window-selection (.getSelection js/window)
        kc     (.-keyCode react-evt)
        kc-local (kc/kc->kw kc)
        intent (interpret-user-intent v kc)]
    (when intent
      (.preventDefault react-evt)
      {:intent intent
       :key-code kc-local
       :value  v
       :target t
       :entity-id (dom/evt->entity-id react-evt)
       :data   (some-> dataset dom/dataset->clj-raw)})))
