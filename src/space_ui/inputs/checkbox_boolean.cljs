(ns space-ui.inputs.checkbox-boolean
  "Checkbox, that submits boolean values

  On change emits value evt
  {:value v, :target t}"
  (:require [reagent.core :as rc]
            [reagent.dom :as rdom]
            [common.functions :as f]))


(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))


(def style-rules
  (list
    [:.space-ui-checkbox
     {:outline        :none
      :font-size      :1em
      :font-weight    :inherit
      :letter-spacing :inherit
      :background     :none
      :border         :none
      :min-height     :1em
      :color          :inherit}]))



(defn face
  [{input-name :comp/name
    :comp/keys
    [value
     id
     css-class
     ^boolean autofocus?
     ^boolean disabled?
     ^boolean required?
     ^IMap attrs ; map with other input attributes
     ^IMap data ; map with data attributes
     ^IFn on-change
     ^IFn on-change--value
     ^IFn on-change-complete]
    :as opts}]
  (let [atom:node (rc/atom nil)
        atom:val (rc/atom value)
        get-cur-value #(some-> @atom:node .-checked)
        get-value-evt (fn [] {:evt/value (get-cur-value) :evt/target @atom:node})
        on-focus-internal (fn [])

        on-change-internal
        (fn [react-evt]
          (let [e (get-value-evt)
                v (:evt/value e)]
            (when (not= v @atom:val)
              (reset! atom:val v)
              (if on-change-complete
                (on-change-complete e)
                (do
                  (if on-change--value
                    (on-change--value v))
                  (if on-change
                    (on-change e)))))))

        on-blur-internal
        (fn [evt]
          (if on-change-complete
            (on-change-complete (get-value-evt))))]

    (rc/create-class
      {:display-name "SpaceUI_Inputs_Checkbox"

       :component-did-mount
       (fn [this]
         (reset! atom:node (rdom/dom-node this)))

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
               data-attrs (render-data-attrs data)]
           [:input.space-ui-checkbox

            (cond->
              {:id          id-str
               :placeholder placeholder
               :class       (if css-class (name css-class))
               :tabIndex    1
               :autoFocus   autofocus?
               :spellCheck  "false"
               :required    required?
               :disabled    disabled?
               :name        input-name
               :type        "checkbox"
               :on-change   on-change-internal
               :on-focus    on-focus-internal
               :on-blur     on-blur-internal
               :defaultChecked -val}

              attrs (f/assign attrs)
              data-attrs (f/assign data-attrs))]))})))

