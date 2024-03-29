(ns space-ui.inputs.color
  (:require [reagent.core :as rc]
            [garden.core :as garden]
            [space-ui.tiny.btn :as btn]
            [space-ui.style.mixins :as mixins]
            [space-ui.style.constants :as sc]))



(defn- get-target-value [evt]
  (.. evt -currentTarget -value))

(def style-rules
  [:.color-input
   {:display :grid
    :align-items :center
    :grid-gap :8px
    :grid-template-columns "auto auto"}
   [:&__input
    {:height        :28px
     :width         :48px
     :cursor        :pointer
     :border        0
     :padding       (sc/px 2)}]
   [:&__reset>.button
    (mixins/size :10px :14px)
    {:stroke-width :30px}]])

(defn face
  [{:comp/keys
    [initial-v on-change id]}]
  (let [v-atom (rc/atom initial-v)

        on-input
        (fn [r-evt]
          (let [new-val (get-target-value r-evt)]
            (reset! v-atom new-val)
            (on-change new-val)))

        -reset
        (fn []
          (reset! v-atom "#ffffff")
          (on-change ""))

        on-change-internal
        (fn [r-evt]
          (let [new-val (get-target-value r-evt)]
            (reset! v-atom new-val)
            (on-change new-val)))]

    (fn []
      [:div.color-input
       [:input.color-input__input
        {:value        @v-atom
         :id           id
         :type         :color
         :placeholder  "Colour expression"
         :on-input     on-input
         :on-change    identity}]
       [:div.color-input__reset
        [btn/icon :icons/close
         #:btn{:on-click -reset
               :title "Click here to reset colour value"}]]])))
