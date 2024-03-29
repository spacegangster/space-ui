(ns space-ui.contenteditable
  (:require [reagent.dom :as r]
            [reagent.core :as rc]
            [space-ui.util.dom :as dom]
            [space-ui.ui-logic.user-intents :as user-intents]
            [space-ui.bem :as vu]))


; text mode atttr

(defn face
  [id
   {:keys
    [^IFn on-change
     ^IFn on-change-complete
     ^IFn on-blur
     ^IFn on-did-mount
     ^IFn on-intent
     ^IFn on-key-down
     ^IFn process-paste
     ^boolean autofocus?
     ^boolean text-mode?
     ^IMap intents]
    :as opts}]
  (user-intents/assert-intents2 intents)
  (let [node (rc/atom nil)
        state-value (atom nil)

        get-cur-value
        (fn []
          (when-let [n @node]
            (if text-mode?
              (.-textContent n)
              (.-innerHTML n))))

        on-key-down (cond
                      intents (rc/partial user-intents/handle-intent-with-intents-map intents)
                      on-intent #(some-> % user-intents/key-down-evt->intent-evt on-intent)
                      on-key-down on-key-down
                      :else identity)

        on-key-up-internal
        (if on-change
          (fn [evt]
            (let [cur-val (get-cur-value)]
              (when (not= cur-val @state-value)
                (on-change {:value cur-val :target @node})))))

        on-input    on-key-up-internal

        on-blur-internal
        (fn [evt]
          (if on-blur (on-blur evt))
          (if on-change-complete
            (on-change-complete
              {:value  (get-cur-value)
               :target @node})))

        on-paste-internal
        (if on-change
          (fn [evt]
            (let [cur-val (get-cur-value)]
              (when (not= cur-val @state-value)
                (let [paste-processed (if process-paste
                                        (process-paste cur-val)
                                        cur-val)]
                  (on-change {:value  paste-processed
                              :target @node}))))))]
    (rc/create-class
      {:display-name "SpaceUI_ContentEditable"

       :component-did-mount
       (fn [this]
         (let [dn (r/dom-node this)]
           (if on-did-mount
             (on-did-mount dn))
           (reset! node dn)))

       :should-component-update
       (fn [this cur-argv [f id next-props :as next-argv]]
         (let [active-element js/document.activeElement
               not-active?    (not= @node active-element)]
           (and not-active? (not= (get-cur-value) (:value next-props)))))

       :reagent-render
       (fn [id {:keys [data                                 ; @param {map} with data attributes
                       on-blur on-focus
                       placeholder css-class value mods field-name] :as opts}] ;; remember to repeat parameters
         (reset! state-value value)
         (let [id-str          (if (keyword? id) (name id) (str id))
               data-attrs      (dom/render-data-attrs data)
               css-class-final (str (vu/bem-str :contenteditable mods) "" css-class)]
           (def css-class)
           [:div.contenteditable
            (cond->
              {:id                             id-str,
               :suppressContentEditableWarning true
               :dangerouslySetInnerHTML        {:__html value}
               :placeholder                    placeholder,
               :class                          css-class-final
               :tabIndex                       1
               :autoFocus                      autofocus?
               :content-editable               true
               :data-text-mode                 text-mode?
               :spellCheck                     "false"
               :on-key-up                      on-key-up-internal
               :on-paste                       on-paste-internal
               :on-input                       on-input
               :on-key-down                    on-key-down
               :on-focus                       on-focus
               :on-blur                        on-blur-internal
               :data-name                      field-name}
              data-attrs (merge data-attrs))]))})))
