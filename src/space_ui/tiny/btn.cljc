(ns space-ui.tiny.btn
  (:require [space-ui.bem :as bem]
            [space-ui.style.main-mixins :as mm]
            [space-ui.style.constants :as sc]
            [space-ui.primitives :as prim]
            [space-ui.svgs :as svgs]))


(def on-click-prop #?(:cljs :on-click :clj :onclick))

(def color:border-blue-skeuo "hsl(203, 59%, 52%)")

(def grad:blue-flat1
  (prim/linear-gradient
    :gradient/to-bottom
    (prim/hsl 213 98 64)
    (prim/hsl 213 98 63)))

(def grad:blue-flat1:active
  (prim/linear-gradient
    :gradient/to-bottom
    (prim/hsl 213 98 62)
    (prim/hsl 213 98 60)))


(def style-rules
  [:.btn
   mm/pane-frame
   {:display         :inline-flex
    :align-items     :center
    :justify-content :center
    :font-size       :14px
    :line-height     1.2
    :color           sc/color-text
    :cursor          :pointer
    :border          "1px solid"
    :padding         (sc/d-step-x-px 1 1.5)}
   [:&__icon
    {:color :inherit
     :margin-right sc/dim-step-px}]
   [:&--textual
    :&--icon
    {:padding   0
     :border    :none
     :color     sc/color-control--textual-default}
    [:&:hover
     {:color sc/color-control--textual-hovered}]]

   [:&:active
    :&--active
    {:background (prim/hsla 0 0 0 0.07)}]

   [:&--round
    mm/button--round]
   [:&--textual
    {:font-size :inherit
     :color :inherit}]

   [:&--cta-blue
    :&--cta-blue:visited
    {:background   grad:blue-flat1
     :color        :white
     :border-color color:border-blue-skeuo}
    [:&:active
     {:background   grad:blue-flat1:active}]]


   [:&--inherit-color
    {:color :inherit}]
   [:&--bigger
    {:font-size :18px
     :letter-spacing :.09em}]
   [:&--danger
    mm/icon--danger]])



(defn btn
  [{:btn/keys
    [icon css-class mods label on-click goal-id
     tooltip title tabindex type attrs]
    :as params}]
  [:button.btn
   (cond-> {:class        (str (bem/bem-str :btn mods) " " css-class)
            :title        (or title tooltip)
            on-click-prop on-click
            :data-goal-id goal-id
            :tabIndex     tabindex
            :type         type}
           attrs (merge attrs))
   (if icon
     [:div.btn__icon
      [svgs/icon icon]])
   (if (string? label)
     [:div.btn__label label])])


(defn cta
  [{:btn/keys
    [icon css-class mods label on-click
     tooltip title tabindex type attrs]
    :as params}]
  (btn (update params :btn/mods conj ::cta)))

(defn cta-blue
  [{:btn/keys
        [icon css-class mods label on-click
         tooltip title tabindex type attrs]
    :as params}]
  (btn (update params :btn/mods conj ::cta-blue)))

(comment
  (cta {:label "hey"}))


(defn icon [icon & [{:btn/keys [title managed-colors? danger? active? on-click round?] :as opts}]]
  [:button
   {:title (or title (name icon))
    :class (bem/bem-str :button :icon
                       (if round? :round)
                       (if danger? :danger)
                       (if active? :active))
    on-click-prop on-click}
   [svgs/icon icon {:active?        active? :danger? danger?
                    :managed-colors managed-colors?}]])
