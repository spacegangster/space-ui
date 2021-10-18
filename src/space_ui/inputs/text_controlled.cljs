(ns space-ui.inputs.text-controlled
  "Text input supporting various :type attr values

  On change emits value evt
  {:evt/value v, :evt/target t}"
  (:require [reagent.core :as rc]
            [reagent.dom :as r]
            [space-ui.ui-logic.user-intents :as user-intents]
            [space-ui.util.functions :as f]))

:input.type/email
:input.type/password
:input.type/text
:input.type/number

(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))


:appearance/transparent
:appearance/text-on-pane-v1

(def class:base "space-ui-input")

(defn face
  [{:comp/keys
    [^string placeholder
     value
     value-type
     value-atom
     id
     css-class css-mods
     ^boolean autofocus?
     ^boolean disabled?
     ^boolean required?
     ^IMap attrs ; map with other input attributes
     ^IMap data ; map with data attributes
     ^keyword input-type
     ^keyword appearance
     ^IMap intents
     ^IFn on-change
     ^IFn on-change--value
     ^IFn parse-fn
     ^IFn format-fn
     ^IFn on-change-complete
     ^IFn on-intent
     ^IFn on-key-down
     ^IFn process-paste]
    :as opts}]

  (let [atom:node (rc/atom nil)

        format-fn (cond
                    format-fn format-fn
                    (= :value-type/vec-of-int value-type) f/format-csv
                    :else identity)
        atom:val (or value-atom
                     (rc/atom (format-fn value)))

        parse-fn (cond
                   parse-fn parse-fn
                   (= :value-type/int value-type) f/parse-int-or-nil
                   (= :value-type/vec-of-int value-type) f/parse-int-csv
                   (= :value-type/float value-type) js/parseFloat
                   (= :input.type/number input-type) f/parse-int-or-nil
                   :else identity)

        get-cur-value #(some-> @atom:node .-value parse-fn)

        get-value-evt (fn [] {:evt/value (get-cur-value) :evt/target @atom:node})

        on-key-down (cond
                      intents (rc/partial user-intents/handle-intent-with-intents-map intents)
                      on-intent #(some-> % user-intents/key-down-evt->intent-evt on-intent)
                      on-key-down on-key-down
                      :else identity)

        on-focus-internal (fn [])

        on-blur-internal
        (fn [evt]
          (if on-change-complete
            (on-change-complete (get-value-evt))))


        on-input
        (fn [evt]
          (let [e (get-value-evt)
                v (:evt/value e)]
            (when (not= v @atom:val)
              (reset! atom:val v)
              (if on-change
                (on-change e))
              (if on-change--value
                (on-change--value v)))))

        on-change-internal (fn [evt] (on-input evt))

        on-paste-internal
        (if on-change
          (fn [evt]
            (let [cur-v (get-cur-value)]
              (when (not= cur-v @atom:val)
                (let [paste-processed (if process-paste
                                        (process-paste cur-v)
                                        cur-v)]
                  (on-change {:evt/value  paste-processed
                              :evt/target @atom:node}))))))

        appearance (or appearance :appearance/text-on-pane-v1)
        css-class-base (str class:base " " class:base "--" (name appearance))]


    (rc/create-class
      {:display-name "SpaceUI_Inputs_TextControlled"

       :component-did-mount
       (fn [this]
         (reset! atom:node (r/dom-node this)))

       :should-component-update
       (fn [this cur-argv [f id next-props :as next-argv]]
         (and (not= @atom:node js/document.activeElement)
              (not= (get-cur-value) (:evt/value next-props))))

       :reagent-render
       (fn [{input-name :comp/name
             :comp/keys
             [^string placeholder
              value
              id
              css-class
              ^boolean autofocus?
              ^boolean disabled?
              ^boolean required?
              ^IMap attrs ; map with other input attributes
              ^IMap data ; map with data attributes
              ^keyword input-type]
             :as        opts}] ;; remember to repeat parameters

         (let [id-str (if (keyword? id) (name id) (str id))
               -val   (or @atom:val "")
               data-attrs (render-data-attrs data)
               css-class (cond-> css-class-base, css-class (str " " (name css-class)))]

           [:input
            (cond->
              {:id          id-str
               :placeholder placeholder
               :class       css-class
               :tabIndex    1
               :autoFocus   autofocus?
               :spellCheck  "false"
               :required    required?
               :disabled    disabled?
               :name        input-name
               :type        input-type
               :on-paste    on-paste-internal
               :on-input    on-input
               :on-change   on-change-internal
               :on-key-down on-key-down
               :on-focus    on-focus-internal
               :value       -val
               :on-blur     on-blur-internal}

              attrs (f/assign attrs)
              data-attrs (f/assign data-attrs))]))})))

