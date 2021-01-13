(ns space-ui.style.main-mixins
  (:require [clojure.string :as str]
            [space-ui.primitives :as sp]
            [space-ui.style.constants :as sc]
            [space-ui.primitives :as prim]))


(defn size
  "produces a map with width and height"
  ([w] (size w w))
  ([w h]
   {:width  (cond-> w (number? w) (str "px"))
    :height (cond-> h (number? h) (str "px"))}))



(def display-centerflex
  {:display :flex
   :align-items :center})

(defn grid--flow-row [gap]
  {:display           :inline-grid
   :align-items       :center
   :grid-column-gap   gap
   :grid-auto-columns :auto
   :grid-auto-flow    :column})

(def display-flex-stretch
  {:display :flex
   :align-items :stretch})

(def pane-frame
  {:border-radius :2px})

(defn important [& strings]
  (str (str/join " " strings) " !important"))


(defn animation-pulse
  "constant pulse"
  [keyframes-name]
  {:animation-duration        :2s
   :animation-timing-function :ease-in-out
   :animation-name            keyframes-name
   :animation-iteration-count :infinite
   :animation-direction       :alternate
   :animation-fill-mode       :forwards})

(defn animation-pulse--one-fill
  "this animation stays after play"
  [keyframes-name]
  {:animation-duration        :1s
   :animation-timing-function :ease-in-out
   :animation-name            keyframes-name
   :animation-iteration-count 1
   :animation-fill-mode       :forwards})



(defn cta-mixin [grad]
 (list
  {:background   grad
   :color        :white}
  #_{:border-color "hsl(0, 0%, 70%)"}
  [:&:active
   {:background grad
    :color      :white
    :filter     "brightness(0.92)"}]))

(def control--hoverable
  (list
    {:cursor    :pointer
     :color      sc/color-control--default
     :stroke     sc/color-control--default
     :fill       sc/color-control--default
     :transition "color .1s ease-in-out, stroke .1s ease-in-out"}
    [:&:hover
      {:color  sc/color-control--hovered
       :stroke sc/color-control--hovered
       :fill   sc/color-control--hovered}]
    [:&--active
      {:color  sc/color-control--active
       :stroke sc/color-control--active
       :fill   sc/color-control--active}]))

(def control--hoverable--half-faded
  (list
    {:cursor :pointer
     :transition "color .1s ease-in-out"
     :opacity 0.4}
    [:&:hover
     {:opacity 1}]))

(defn fullscreen [& [dim-space]]
  (let [dim-space (or dim-space "0px")]
    {:position  :fixed
     :top       dim-space
     :bottom    dim-space
     :left      dim-space
     :right     dim-space}))

(defn fullscreen-abs [dim-space]
  (let [dim-space (or dim-space "0px")]
    {:position  :absolute
     :top       dim-space
     :bottom    dim-space
     :left      dim-space
     :right     dim-space}))

(def button--round
  [:&
   {:height        :25px
    :width         :25px
    :border-radius :50%
    :border        (str "1px solid " sc/color-control--textual-default)
    :padding        0}
   [:&:hover
    {:border-color sc/color-control--textual-hovered}]])


(def color-text-radiance--initial (prim/hsla 0 0 100 0.6))
(def color-text-radiance--stage-1 (prim/hsla 0 0 100 0.8))
(def color-text-radiance--stage-2 (prim/hsla 0 0 100 0.9))

(def text-radiance--on
  {:color        color-text-radiance--stage-2
   :text-shadow  (str "0 0 2px "color-text-radiance--stage-2)})

(def text-radiance
  [:&
   {:color      color-text-radiance--initial
    :transition "all .2s ease-in-out"}
   [:&:hover
    text-radiance--on]])

(def grid-area--all
  {:grid-area "1 / 1 / last-line / end"})

(defn focusable [& [color-bg-focused]]
  (let [color-bg-focused (or color-bg-focused sc/color-lightable--rank-1)]
   [:&.g-focus
    {:background color-bg-focused}]))



(def icon--danger
  (let [col (sp/hsl 0 80 50)]
    [:&
     {:transition "all .3s ease-in-out"}
     [:&:hover
      {:text-shadow  "0 0 4px red"
       :color        col
       :stroke       col
       :fill         col}]]))


(def dual-scroll
  (list
    [:&--scroll-native
     {:height     :100%
      :overflow-x :hidden
      :overflow-y :scroll}]
    [:&--scroll-gemini
     {:height :100%}]))

(def pane
  (list
    (merge
      pane-frame
      {:background  sc/color-lightable--base
       :transition  "background .1s ease-in-out"
       :will-change :background})
    [:&.g-hover--rank-1
     {:background (important sc/color-lightable--rank-1)}]
    [:&.g-hover--rank-2
     {:background (important sc/color-lightable--rank-2)}]
    [:&.g-focus--rank-1
     {:background sc/color-lightable--rank-1}]
    [:&.g-focus--rank-2
     {:background sc/color-lightable--rank-2}]))

(def filler
  (let [own-rules
        {:height          "calc(100vh - 112px)"
         :display         :flex
         :align-items     :center
         :line-height     :1.6
         :justify-content :center
         :background      sc/color-lightable--filler
         :padding         (str "0 " sc/dim-item-side-pad)
         :color           sc/color-text--placeholders}]
    (into (list own-rules) pane)))

(def nav-item
  (into (list {:cursor :pointer}) pane))

(def pane--opaque
  (merge pane-frame
         {:background sc/color-lightable--opaque}))

(def placeholded
  [["&:empty::before"
    {:content "attr(placeholder)"
     :color   sc/color-text--placeholders}]
   ["&:focus:empty::before"
    {:content ""}]])

(defn webkit-text-w-bg [background]
  {:background              background
   :-webkit-background-clip :text
   :background-clip         :text
   :-webkit-text-fill-color :transparent
   :text-fill-color         :transparent})
