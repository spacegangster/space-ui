(ns space-ui.inputs.select-multiple
  "Input allowing to select multiple options.
   Submits a set of selected values."
  (:require [garden.core :as garden]
            [space-ui.util.functions :as ui.f])
  #?(:cljs
     (:require [commons.logging :as log]
               [reagent.core :as rc]
               [commons.functions :as f]))
  #?(:clj (:import (java.util Set))))


(def class:base "select-multi-mk1")
(def class:item__input "select-multi-mk1__item__input")
(def class:item__label "select-multi-mk1__item__label")

(def sel:base (str "." class:base))
(def sel:item__input (str "." class:item__input))
(def sel:item__label (str "." class:item__label))

(def style-rules-buttons
  (list
    [sel:base
     {:display           :grid
      :grid-auto-flow    :column
      :grid-gap          :4px
      :grid-auto-columns "min-content"}

     [:&__item
      [:&__input
       {:display :none}]
      [:&__label]]]

    [(str sel:item__input":checked + " sel:item__label)
     {:color       :blue}]
      ;:font-weight :bold}]

    [:.button]))

(def style-buttons
  [:style
   (garden/css style-rules-buttons)])


(defn on-change-internal
  [value-parse on-change on-change--value atom:val react-evt]
  #?(:cljs
     (let [target (^js .-target react-evt)
           val-raw (not-empty (^js .-value target))
           checked? (^js .-checked target)
           val-parsed (value-parse val-raw)
           new-val (swap! atom:val (if checked? conj disj) val-parsed)
           e #:evt{:type   :event-types/change
                   :value  new-val
                   :target target}]
       (when on-change
         (on-change e))
       (when on-change--value
         (on-change--value new-val)))))


(defn face-simple
  [{input-name :comp/name
    :comp/keys
    [on-change on-change--value
     val-parse val-format
     options value]
    :as opts}
   atom:val]
  (let [vals-set @atom:val]

    [:div.select-multi-mk1
     (for [item options]
       (let [id-str (val-format (:option/value item))
             item-id (str "multi-select-" id-str)
             item-name (str input-name "[" id-str "]")
             on-item-change* #?(:cljs (rc/partial on-change-internal val-parse on-change on-change--value atom:val)
                                :clj nil)]

         ^{:key id-str}
         [:div.select-multi-mk1__item
          [:input.select-multi-mk1__item__input
           (cond->
             {:type    "checkbox"
              :id      item-id
              :name    item-name
              :checked (contains? vals-set (:option/value item))
              :value   id-str}
             on-item-change* (assoc :on-change on-item-change*))]
          [:label.select-multi-mk1__item__label.button
           {:for item-id}
           (:option/label item)]]))]))


(defn face
  "Note that value is a set"
  [{input-name :comp/name
    :comp/keys
    [on-change on-change--value
     options
     value]  ; set
    :as opts}]
  (let [value (or value #{})
        atom:val #?(:cljs (rc/atom value) :clj (atom value))
        val-format (ui.f/options->format options)
        val-parse (ui.f/options->transform options)
        opts (assoc opts :comp/val-parse val-parse
                         :comp/val-format val-format)]

    (fn []
      ^{:key (str @atom:val)}
      [face-simple opts atom:val])))

