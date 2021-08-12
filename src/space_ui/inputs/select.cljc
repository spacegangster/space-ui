(ns space-ui.inputs.select
  #?(:cljs (:require [reagent.core :as rc]
                     [reagent.dom]))
  (:require [space-ui.util.functions :as ui.f]
            [space-ui.style.constants :as sc]))


(def style-rules
  [:.space-ui-select
   {:font-size      :1em
    :font-weight    :inherit
    :letter-spacing :inherit
    ;:background     :none
    ;:border         :none
    :min-height     :1em
    :color          :inherit
    :border-radius  :3px
    :border-color   sc/color:border:input-on-pane}
   {:padding :4px
    :max-width :100%}])

(defn- option
  [{:option/keys [value label]}]
  [:option {:value value} label])


(defn on-change-internal
  [value-transform on-change on-change--value react-evt]
  #?(:cljs
     (let [target (^js .-target react-evt)
           val-raw (not-empty (^js .-value target))
           val-parsed (value-transform val-raw)
           e #:evt{:type   :event-types/change
                   :value  val-parsed
                   :target target}]
       (when on-change
         (on-change e))
       (when on-change--value
         (on-change--value val-parsed)))))


(defn face
  [{field-name :comp/name
    :comp/keys
    [id
     value
     options
     auto-focus?
     on-change
     on-change--value
     css-class]}]
  (let [value-transform (ui.f/options->transform options)
        value-format    (ui.f/options->format options)
        val-fmt (value-format value)
        on-change-internal1
        #?(:cljs (rc/partial on-change-internal value-transform on-change on-change--value)
           :clj nil)]

    [:select.space-ui-select
     (cond->
       {:id         id
        :auto-focus auto-focus?
        :name       field-name
        :value      val-fmt}
       css-class (assoc :class css-class)
       on-change-internal1 (assoc :on-change on-change-internal1))

     (for [item options]
       (if-let [opt-group-items (:optgroup/items item)]

         ; w optgroup
         ^{:key (:optgroup/label item)}
         [:optgroup
          {:label (:optgroup/label item)}
          (for [item opt-group-items]
            ^{:key (:option/value item)}
            [option item])]

         ; simple options
         ^{:key (or (:option/value item) (:option/label item))}
         [option (update item :option/value value-format)]))]))

(comment
  (face #:comp{:name "zone" :id "son"}))
