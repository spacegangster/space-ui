(ns space-ui.inputs.text
  "Text input supporting various :type attr values

  On change emits value evt
  {:evt/value v, :evt/target t}"
  (:require [reagent.core :as rc]
            [reagent.dom :as r]
            [space-ui.ui-logic.user-intents :as user-intents]
            [commons.logging :as log]
            [common.functions :as f]
            [commons.functions :as u]
            [space-ui.style.constants :as sc]))

:input.type/email
:input.type/password
:input.type/text
:input.type/time
:input.type/date
:input.type/number

(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))

(defn autofill-fix-text
  "Fixes autofill text"
  [attrs]
  ; autofill background color can be set with &:-webkit-autofill {box-shadow inset}
  [".space-ui-input:-webkit-autofill::first-line"
   attrs])

(defn fix-placeholder [attrs]
  [".space-ui-input::placeholder"
   attrs])


(def style-rules
  (list
    [:.space-ui-input
     {:width          :100%
      :outline        :none
      :font-size      :1em
      :font-weight    :inherit
      :letter-spacing :inherit
      :background     :none
      :border         :none
      :min-height     :1em
      :min-width      :1.5rem
      :color          :inherit
      :text-overflow  :ellipsis}

     ["&[type=text]:empty"
      "&[type=password]:empty"
      "&[type=number]:empty"
      "&[type=email]:empty"
      {:min-width :220px}
      ["&::before"
       {:content "attr(placeholder)"}]]
     ["&[type=number]:empty"
      {:min-width :40px}]
     ["&::placeholder"
      {:color "hsl(0, 0%, 65%)"}]

     [:&--text-on-pane-v1
      :&--blue-on-white
      {:height        :100%
       :border-radius "2px 2px 0 0"
       :padding       "4px"
       :border-bottom "1px solid #aaa"
       :background    sc/color:bg:input-on-pane:base}
      [:&:focus
       {:background sc/color:bg:input-on-pane:focus}]]
     [:&--blue-on-white
      {:background sc/color:bg:input-on-pane--blue}
      [:&:focus
       {:background sc/color:bg:input-on-pane--blue:focus}]]]

    (autofill-fix-text
      {:font-size :1em})))


:appearance/transparent
:appearance/text-on-pane-v1
:appearance/blue-on-white

(def class:base "space-ui-input")


(defn face
  [{:comp/keys
    [^string placeholder
     value
     value-type
     id
     css-class
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
                    (= :value-type/vec-of-int value-type) u/format-csv
                    :else identity)
        atom:val (rc/atom (format-fn value))

        parse-fn (cond
                   parse-fn parse-fn
                   (= :value-type/int value-type) u/parse-int-or-nil
                   (= :value-type/vec-of-int value-type) u/parse-int-csv
                   (= :value-type/float value-type) js/parseFloat
                   (= :input.type/number input-type) u/parse-int-or-nil
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
      {:display-name "SpaceUI_Inputs_Text"

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
              ^boolean disabled?
              ^boolean required?
              ^IMap attrs ; map with other input attributes
              ^IMap data ; map with data attributes
              ^keyword input-type]
             :as        opts}] ;; remember to repeat parameters

         (let [id-str (if (keyword? id) (name id) (str id))
               -val @atom:val
               data-attrs (render-data-attrs data)
               css-class (cond-> css-class-base, css-class (str " " (name css-class)))]

           [:input
            (cond->
              {:id          id-str
               :placeholder placeholder
               :class       css-class
               :tabIndex    1
               :autoFocus   (:autofocus opts)
               :spellCheck  "false"
               :required    required?
               :disabled    disabled?
               :name        input-name
               :type        input-type
               :on-paste    on-paste-internal
               :on-input    on-input
               :on-key-down on-key-down
               :on-focus    on-focus-internal
               :defaultValue -val
               :on-blur     on-blur-internal}

              attrs (f/assign attrs)
              data-attrs (f/assign data-attrs))]))})))

