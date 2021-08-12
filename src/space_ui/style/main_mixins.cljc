(ns space-ui.style.main-mixins
  (:require [clojure.string :as str]
            [space-ui.primitives :as sp]
            [space-ui.style.constants :as sc]
            [common.functions :as f]
            [space-ui.primitives :as prim]))


(defn size
  "produces a map with width and height"
  ([w] (size w w))
  ([w h]
   {:width  (cond-> w (number? w) (str "px"))
    :height (cond-> h (number? h) (str "px"))}))

(def rotate-90deg
  {:transform-origin "50% 0"
   :transform        "rotate(-90deg)"
   :will-change      "transform"
   :transition       "transform 180ms cubic-bezier(.47,1.64,.41,.8)"})

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

(def pane-frame--larger
  {:border-radius "4px !important"})

(defn important [& strings]
  (str (str/join " " strings) " !important"))


(def form-title--thin
  {:letter-spacing :0.01em
   :word-spacing   :0.07em
   :width          :100%
   :position       :relative})

(def form-title--bold
  {:letter-spacing "0.0em"
   :word-spacing   "0.05em"
   :font-weight    "500"
   :font-family    "system-ui"
   :color          "hsl(0deg 0% 44%)"
   :width          "100%"
   :position       "relative"
   :font-size      "26px"})


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


(def border-default
  {:border (str "1px solid " sc/color:border:control-on-pane)})

(def border-btn-round
  {:border (str "1px solid " sc/color:border:btn-round-on-pane)})

(def button--round
  [:&
   {:height        :25px
    :width         :25px
    :border-radius :50%}
   {:padding        0}
   border-btn-round
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
  "mixin defining pane borders and background.
  Also supports hover and focus modifiers."
  (list
    (f/assign
      pane-frame
      {:background  sc/color-lightable
       :transition  "background .1s ease-in-out"
       :will-change :background})

    ; rank1 is the most intense one
    [:&.g-hover--rank-1
     {:background (important sc/color:lightable-opaque--rank-1)}]
    [:&.g-hover--rank-2
     {:background (important sc/color:lightable-opaque--rank-2)}]

    ; rank1 is the most intense one
    [:&.g-focus--rank-1
     {:background sc/color:lightable-opaque--rank-1}]
    [:&.g-focus--rank-2
     {:background sc/color:lightable-opaque--rank-2}]))


(def pane-alpha
  "same as pane but w transparent background
  Also supports hover and focus modifiers"
  (list
    (f/assign
      pane-frame
      {:background  sc/color-lightable-a--base
       :transition  "background .1s ease-in-out"
       :will-change :background})

    ; rank1 is the most intense one
    [:&.g-hover--rank-1
     {:background (important sc/color-lightable--rank-1)}]
    [:&.g-hover--rank-2
     {:background (important sc/color:lightable--rank-2)}]

    ; rank1 is the most intense one
    [:&.g-focus--rank-1
     {:background sc/color:lightable-opaque--rank-1}]
    [:&.g-focus--rank-2
     {:background sc/color:lightable-opaque--rank-2}]))



(def mixin:pane-on-light-adaptations
  {:background sc/color:bg:pane-on-light
   :box-shadow "0 0 1px 0px hsl(211deg 35% 86%)"
   :transition "background .1s ease-in-out"})

(def mixin:pane-on-light-adaptations--stronger
  {:background sc/color:bg:pane-on-light
   :box-shadow "0 0 4px 0px hsl(211deg 35% 86%)"
   :transition "background .1s ease-in-out"})

(def pane--on-light
  "mixin defining pane borders and background.
  Also supports hover and focus modifiers
  plain list mixin"
  (list (merge pane-frame mixin:pane-on-light-adaptations--stronger)))


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


(def label--secondary
  "e.g. for task field labels"
  {:font-size :13px
   :color     "hsl(0, 0%, 60%)"})

(def label--secondary-pad-b
  "e.g. for task field labels"
  {:display        :block
   :padding-bottom :4px})


(def nav-item
  (cons
    {:height sc/dim:grid:nav-item-height-px
     :cursor :pointer}
    pane))


(def pane--opaque
  (merge pane-frame
         {:background sc/color-lightable}))

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
