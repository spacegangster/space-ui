(ns space-ui.inputs.radio1
  (:require [garden.core :as garden]
            [space-ui.util.functions :as ui.f])
  #?(:cljs
     (:require [commons.logging :as log]
               [reagent.core :as rc]
               [commons.functions :as f])))


(def style-rules-buttons
  (list
    [:.radio
     {:display           :grid
      :grid-auto-flow    :column
      :grid-gap          "16px"
      :grid-auto-columns "min-content"}]

    [:.radio-item
     [:&__input
      {:display :none}]
     [:&__label]]

    [".radio-item__input:checked + .radio-item__label"
     {:color       :blue}]))
      ;:font-weight :bold}]

(def style-buttons
  [:style
   (garden/css style-rules-buttons)])


(defn on-change-internal
  [value-parse on-change on-change--value atom react-evt]
  #?(:cljs
     (let [target (^js .-target react-evt)
           val-raw (not-empty (^js .-value target))
           val-parsed (value-parse val-raw)
           e #:evt{:type   :event-types/change
                   :value  val-parsed
                   :target target}]
       (reset! atom val-parsed)
       (when on-change
         (on-change e))
       (when on-change--value
         (on-change--value val-parsed)))))


(defn face-simple
  [{input-name :comp/name
    :comp/keys
    [on-change on-change--value
     val-parse val-format
     css-class
     options value]
    :as opts}
   atom:val]
  (let [val-selected @atom:val
        on-change* #?(:cljs (rc/partial on-change-internal val-parse on-change on-change--value atom:val)
                      :clj nil)]

    [:div.radio
     {:class css-class}

     (for [item options]
       (let [val-str (val-format (:option/value item))]
         ^{:key val-str}
         [:div.radio-item
          [:input.radio-item__input
           (cond->
             {:type      "radio"
              :id        val-str
              :name      input-name
              :checked   (= (:option/value item) val-selected)
              :value     val-str}
             on-change* (assoc :on-change on-change*))]
          [:label.radio-item__label.button
           {:for val-str}
           (:option/label item)]]))]))


(defn face
  [{input-name :comp/name
    :comp/keys
    [on-change on-change--value
     options value] :as opts}]
  (let [atom:val #?(:cljs (rc/atom value) :clj (atom value))
        val-format (ui.f/options->format options)
        val-parse (ui.f/options->transform options)
        opts (assoc opts :comp/val-parse val-parse
                         :comp/val-format val-format)]

    (fn []
      ^{:key (str @atom:val)}
      [face-simple opts atom:val])))
