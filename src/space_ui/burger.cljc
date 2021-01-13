(ns space-ui.burger
  "Burger for page top position"
  (:require [garden.core :as garden]))


(def style-rules
  [:.burger
   {:position :relative
    :width :40px
    :height :40px}

   [:&__items
    {:display               :none
     :position              :fixed
     :top                   :5rem
     :right                 :1rem
     :width                 "calc(100% - 2rem)"
     :height                "calc(100% - 6rem)"
     :padding               :1rem
     :border                "1px solid white"
     :place-items           "start end"
     :grid-auto-flow        :row
     :grid-template-columns "minmax(100px, auto)"
     :grid-auto-rows        :min-content
     :grid-gap              :24px}]

   [:.burger__check
    {:display :none}]
   [:.burger__check:checked+.burger__menu
    [:>.burger__items
     {:display :grid}]]

   [:&__label
    {:display         :flex
     :width           :40px
     :height          :40px
     :justify-content :space-around
     :align-items     :center
     :flex-direction  :column
     :position        :relative
     :cursor          :pointer
     :opacity         0.8
     :transition      "all .1s ease-in-out"}
    #_(gs/at-media {:min-width sc/dim-bp-mobile}
                   [:&:hover
                    {:height    :1.6em
                     :opacity   1
                     :transform "translateY(-0.15em)"}])
    [:&__row
     {:height      :2px
      :flex        "0 0 auto"
      :width       :24px
      :background  "hsl(0, 0%, 90%)"
      :transition  "all .2s ease-in-out"
      :transform-origin :50%
      :will-change :transform}
     [:&--legal
      {:background "hsl(0, 0%, 20%) !important"}]]]

   [:.burger__check:checked+.burger__menu>.burger__label
    {:display :block}
    [:>.burger__label__row
     {:transform "rotateZ(45deg)"
      :left      :8px
      :position  :absolute
      :background "hsl(0, 0%, 80%)"
      :top       :19px}
     [:&--closer
      {:transform "rotateZ(-45deg)"}]]]])



(defn- button
  [{:burg/keys [colorscheme? for on-click title open? mod]}]
  [:label.burger__label
   {:title title
    :for for
    :on-click on-click
    :class (str (str " burger__label--" mod)
                (if open?
                  " burger--open"
                  " burger--closed"))}

   [:div.burger__label__row
    {:class (str (str " burger__label__row--" mod)
                 (if colorscheme? "g-colorscheme-background"))}]
   [:div.burger__label__row.burger__label__row--closer
    {:class (str (str " burger__label__row--" mod)
                 (if colorscheme? "g-colorscheme-background"))}]])

(defn face
  "basic burger menu impl"
  [{:burg/keys
    [^ICollection items
     ^js/String mod] :as opts}]

  [:div.burger {:class (str "burger--" mod)}
   [:style (garden/css style-rules)]
   [:input.burger__check {:type :checkbox :id ::__burger-switch}]
   [:div.burger__menu
    (button {:burg/for ::__burger-switch :burg/mod mod})
    [:div.burger__items
     {:class (str "burger__items--" mod)}
     items]]])
