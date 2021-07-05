(ns space-ui.input
  "Deprecated. Use space-ui.inputs.text instead.

  Text input supporting various :type attr values
  Can also be a checkbox, but please don't use it

  On change emits value evt
  {:value v, :target t}"
  (:require [reagent.core :as rc]
            [reagent.dom :as r]
            [space-ui.ui-logic.user-intents :as user-intents]
            [commons.logging :as log]
            [commons.functions :as u]))

:input.type/email
:input.type/password
:input.type/text
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
      :color          :inherit}
     ["&[type=text]:empty"
      "&[type=password]:empty"
      "&[type=email]:empty"
      {:min-width :220px}
      ["&::before"
       {:content "attr(placeholder)"}]]
     ["&::placeholder"
      {:color "hsl(0, 0%, 65%)"}]]
    (autofill-fix-text
      {:font-size :1em})))



(defn
  ^{:deprecated true}
  face
  "Use space-ui.inputs.text instead"
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
     ^IMap intents
     ^IFn on-change
     ^IFn on-change--value
     ^IFn parse-fn
     ^IFn on-change-complete
     ^IFn on-intent
     ^IFn on-key-down
     ^IFn process-paste]
    :as opts}]
  (let [atom:node (rc/atom nil)
        atom:val (rc/atom value)

        switchable-type? (= :input.type/checkbox input-type)
        parse-fn (cond
                   parse-fn parse-fn
                   (= :value-type/int value-type) u/parse-int-or-nil
                   (= :value-type/float value-type) js/parseFloat
                   (= :input.type/number input-type) u/parse-int-or-nil
                   :else identity)

        get-cur-value
        (case input-type
          :input.type/checkbox
          #(some-> @atom:node .-checked)
          #(some-> @atom:node .-value parse-fn))

        get-value-evt (fn [] {:value (get-cur-value) :target @atom:node})

        on-key-down (cond
                      intents (rc/partial user-intents/handle-intent-with-intents-map intents)
                      on-intent #(some-> % user-intents/key-down-evt->intent-evt on-intent)
                      on-key-down on-key-down
                      :else identity)

        on-focus-internal (fn [])

        on-change-internal
        (if switchable-type?
          (fn [react-evt]
            (let [e (get-value-evt)
                  v (:value e)]
              (when (not= v @atom:val)
                (reset! atom:val v)
                (if on-change-complete
                  (on-change-complete e)
                  (do
                    (if on-change--value
                      (on-change--value v))
                    (if on-change
                      (on-change e)))))))
          identity)

        on-blur-internal
        (fn [evt]
          (if on-change-complete
            (on-change-complete (get-value-evt))))

        on-input
        (if switchable-type?
          identity
          (fn [evt]
            (let [e (get-value-evt)
                  v (:value e)]
              (when (not= v @atom:val)
                (reset! atom:val v)
                (if on-change
                  (on-change e))
                (if on-change--value
                  (on-change--value v))))))

        on-paste-internal
        (if on-change
          (fn [evt]
            (let [cur-v (get-cur-value)]
              (when (not= cur-v @atom:val)
                (let [paste-processed (if process-paste
                                        (process-paste cur-v)
                                        cur-v)]
                  (on-change {:value  paste-processed
                              :target @atom:node}))))))]
    (rc/create-class
      {:display-name "SpaceUI_Input"

       :component-did-mount
                     (fn [this]
                       (reset! atom:node (r/dom-node this)))

       :should-component-update
                     (fn [this cur-argv [f id next-props :as next-argv]]
                       (and (not= @atom:node js/document.activeElement)
                            (not= (get-cur-value) (:value next-props))))

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
               data-attrs (render-data-attrs data)]
           [:input.space-ui-input

            (cond->
              {:id          id-str
               :placeholder placeholder
               :class       (if css-class (name css-class))
               :tabIndex    1
               :autoFocus   (:autofocus opts)
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
               :on-blur     on-blur-internal}

              (not switchable-type?)
              (assoc :defaultValue -val)
              ;
              switchable-type?
              (assoc :defaultChecked -val)
              ;
              attrs (merge attrs)
              data-attrs (merge data-attrs))]))})))

