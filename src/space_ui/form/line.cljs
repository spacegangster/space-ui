(ns space-ui.form.line
  "Displays line with label and input.

   Layouts:
   - mobile (narrower than 500px) – label on top, input at the bottom
   - desktop (wider than 500px) – label on the left, input on the right

   Supports async validation with status reporting"
  (:require [clojure.string :as str]
            [garden.stylesheet :as gs]
            [space-ui.style.mixins :as mixins]
            [space-ui.style.constants :as sc]
            [space-ui.bem :as vu]
            [space-ui.primitives :as prim]
            [space-ui.inputs.text :as input]
            [space-ui.inputs.checkbox-boolean :as checkbox]
            [space-ui.bem :as bem]
            [reagent.core :as rc]
            [commons.logging :as log]))


(def style-rules
  [:.line
   {:display       :grid
    :grid-template "'label control' auto / 0.4fr 0.6fr"
    :grid-gap      (sc/d-step-x-px 1)
    :place-items   "center stretch"
    :width         :100%
    :height        :100%}

   [:&--hinted
    {:grid-template
     (prim/grid-template
       ["label control" :auto]
       ["hint hint" :auto]
       [:0.4fr :0.6fr])}]

   [:&--auto-cols
    {:grid-template-columns "auto auto"
     :grid-auto-columns :auto}]

   [:&--input-only
    {:grid-template "'control' auto / auto"}
    [:&.line--hinted
     {:grid-template
      (prim/grid-template
        ["control" :auto]
        ["hint" :auto]
        [:auto])}]]

   [:&--textarea
    {:min-height (sc/d-step-x-px 6)}]

   [:&--checkbox
    {:grid-template (prim/grid-template
                      ["control label" :auto]
                      [:18px :auto])}]

   (gs/at-media sc/mq:phone-and-smaller
     [:&--wrap-mobile
      {:display :grid
       :grid-template
       (prim/grid-template
          ["label" :auto]
          ["control" :auto]
          [:auto])}]
     [:&--wrap-mobile.line--hinted
      {:grid-template
       (prim/grid-template
         ["label" :auto]
         ["control" :auto]
         ["hint" :auto]
         [:auto])}])

   [:&__label
    {:grid-area      :label
     :letter-spacing :0.03em
     :place-self     "center start"}
    {:min-width :100px}
    ;; maybe you'll need to compensate validation status height later
    [:&--textarea
     {:place-self :start}]]

   [:&__control
    {:grid-area  :control
     :place-self "stretch"}
    {:min-width :10px}
    ["> .contenteditable"
     mixins/placeholded]

    [:&__status
     {:height        :16px
      :font-size     :12px
      :line-height   1.2
      :overflow      :hidden
      :tex-overflow :ellipsis}]

    [:&--checkbox
     {:height     :18px
      :position   :relative
      :place-self "center stretch"
      :top        :2px}]

    [:&--select
     {:min-width :100px}]

    [:&--color
     #_{:display :inline-grid
        :place-self "center start"
        :grid-template-columns "auto auto"
        :grid-gap (sc/d-step-x-px 0.5)}
     {:display :block
      :place-self "stretch"}]]

   [:&__hint
    {:grid-area  :hint
     :margin-top :0.5rem
     :color sc/color-text--placeholders}
    [:&--sticky
     {:margin-top 0}]
    [:&--right
     {:text-align :right}]]])


(def controls
  {:input.type/text     input/face
   :input.type/textarea input/face
   :input.type/email    input/face
   :input.type/checkbox checkbox/face
   :input.type/password input/face
   :input.type/color    input/face})


(def wide-input?
  #{:input.type/password :input.type/text :input.type/select :input.type/email})

(defn face
  "Displays line with label and input.

   Layouts:
   - mobile (narrower than 500px) – label on top, input at the bottom
   - desktop (wider than 500px) – label on the left, input on the right"
  [{input-name :comp/name
    :comp/keys
    [on-blur on-blur-capture on-reset
     ^IFn on-change           ; (fn [{:evt/keys [value target]}])
     ^IFn on-change-complete  ; (fn [{:evt/keys [value target]}])
     ^IFn on-change--value    ; (fn [value])
     ^IFn process-paste
     ^IFn validate
     ^string id
     ^string css-class
     label ; string or hiccup block
     ^keyword appearance
     ^string placeholder
     ^boolean read-only?
     ^boolean required?
     ^boolean no-wrap?
     ^IMap input-attrs
     value
     control
     hint hint-right
     ^keyword input-type]}]
  (let [input-type       (or input-type :input.type/text)
        input-name       (or input-name id)
        use-value-key?   (not (#{:input.type/color} input-type))
        control-first?   (#{:input.type/checkbox} input-type)
        wrap-mobile?     (and (not no-wrap?) (wide-input? input-type))
        label-elem       [:div (bem/bem :line__label input-type
                                        (if validate "compensate-validation"))
                          label]
        atom:err         (rc/atom nil)
        -on-validate-err (fn [err value] (reset! atom:err [err value]))
        validate-local   (fn [value on-validated]
                           (validate value
                                     (fn [err val]
                                       (log/log "validated" err val)
                                       (if err
                                         (-on-validate-err err val)
                                         (do
                                           (reset! atom:err nil)
                                           (on-validated err val))))))
        control
        (or control
            [(get controls input-type)
             (cond->
               {:comp/value              value
                :comp/id                 id
                :comp/disabled?          read-only?
                :comp/required?          required?
                :comp/input-type         input-type
                :comp/placeholder        placeholder
                :comp/appearance         appearance
                :comp/process-paste      process-paste
                :comp/on-change          on-change
                :comp/on-validation-err  -on-validate-err
                :comp/on-change--value   on-change--value
                :comp/on-change-complete on-change-complete
                :comp/name               input-name}
               validate    (assoc :comp/validate validate-local)
               input-attrs (assoc :comp/attrs input-attrs))])


        css-class
        (str " " (some-> css-class name)
             " " (str "line--" (name input-type))
             " " (if-not label "line--input-only")
             " " (if wrap-mobile? "line--wrap-mobile")
             " " (if (or hint hint-right) (str "line--hinted")))]

    (fn []

      (let [control-elem
            (if-not (= :input.type/none input-type)
              [:div (vu/bem :line__control (name input-type))
               control

               (if validate
                 [:div.line__control__status
                  (if-let [[err val] @atom:err]
                    (str "Can't take " val))])

               (when (and on-reset (not-empty value))
                 [:button.line__reset
                  {:title    (if-not label
                               "reset"
                               (str/join " " (cons "reset" (filter string? label))))
                   :on-click on-reset} "x"])])]

        [:div.line
         {:class           css-class
          :on-blur-capture on-blur-capture
          :on-blur         on-blur
          :title           label}

         (if (and label (not control-first?))
           label-elem)

         ^{:key (if use-value-key? value id)}
         control-elem

         (if (and label control-first?)
           label-elem)

         (if hint
           [:div.line__hint hint])
         (if hint-right
           [:div (vu/bem :line__hint :right :sticky) hint-right])]))))


