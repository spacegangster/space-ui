(ns space-ui.inputs.radio1
  (:require [garden.core :as garden])
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
     {:color       :blue
      :font-weight :bold}]

    [:.button]))

(def style-buttons
  [:style
   (garden/css style-rules-buttons)])


#?(:cljs
   (defn on-change--kw [atom react-evt]
     (let [t (.-target react-evt)
           v-raw (.-value t)
           v-parsed (f/keyword<-string v-raw)]
       ;(.persist react-evt)
       (reset! atom v-parsed)
       (log/info ::change #_react-evt v-parsed))))


(defn face-simple
  [{input-name :comp/name
    :comp/keys [keyword-mode? options value]
    :as opts}
   atom:val]
  [:div.radio
   (for [item options]
     (let [val-str (str (:option/value item))]
       ^{:key val-str}
       [:div.radio-item
        [:input.radio-item__input
         {:type      "radio"
          :id        val-str
          :name      input-name
          :on-change #?(:cljs (if keyword-mode?
                                (rc/partial on-change--kw atom:val))
                        :clj  nil)
          :checked   (= (:option/value item) @atom:val)
          :value     val-str}]
        [:label.radio-item__label.button
         {:for val-str}
         (:option/label item)]]))])

(defn face
  [{input-name :comp/name
    :comp/keys [keyword-mode? options value] :as opts}]
  (let [atom:val #?(:cljs (rc/atom value) :clj (atom value))]
    (fn []
      ^{:key (str @atom:val)}
      [face-simple opts atom:val])))
