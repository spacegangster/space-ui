(ns space-ui.style.mixins
  (:require [clojure.string :as s]
            ["tinycolor2" :as tc]
            [space-ui.primitives :as sp]
            [space-ui.style.constants :as c]
            [garden.core :as garden]
            [space-ui.style.constants :as sc]
            [space-ui.primitives :as prim]))

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
  (str (s/join " " strings) " !important"))

(defn animation-bsb-pulse [keyframes-name]
  (let [kfn  (str "bsb-pulse-" keyframes-name)]
    {:animation (str "1s ease-in-out " kfn " infinite")
     :animation-fill-mode :forwards}))

(defn animation-bsb-pulse-one [keyframes-name]
  (let [kfn  (str "bsb-pulse-" keyframes-name)]
    {:animation (str "1s ease-in-out " kfn " 1")
     :animation-fill-mode :forwards}))

(defn animation-bs-pulse [keyframes-name]
  (let [kfn  (str "bs-pulse-" keyframes-name)]
    {:animation (str "1s ease-in-out " kfn " infinite")
     :animation-fill-mode :forwards}))

(defn animation-bs-pulse-one [keyframes-name]
  (let [kfn  (str "bs-pulse-" keyframes-name)]
    {:animation (str "1s ease-in-out " kfn " 1")
     :animation-fill-mode :forwards}))


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


(defn size
  ([w]
   {:width w
    :height w})
  ([w h]
   {:width w
    :height h}))

(def grid-area--all
  {:grid-area "1 / 1 / last-line / end"})

(defn focusable [& [color-bg-focused]]
  (let [color-bg-focused (or color-bg-focused sc/color-lightable--rank-1)]
   [:&.g-focus
    {:background color-bg-focused}]))

(defn persistence-indicated--focus [sel]
  (list
    [(str sel "--valid")
     (animation-bsb-pulse-one "valid")]
    [(str sel "--in-progress")
     (animation-bsb-pulse "processing")]
    [(str sel "--error")
     (animation-bsb-pulse-one "error")]))

(def persistence-indicated
  (list
    [:&--valid
     (animation-bs-pulse-one "valid")]
    [:&--in-progress
     (animation-bs-pulse "processing")]
    [:&--error
     (animation-bs-pulse-one "error")]))

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
      {:background c/color-lightable--base
       :transition "background .1s ease-in-out"
       :will-change :background})
    [:&.g-hover--rank-1
     {:background (important c/color-lightable--rank-1)}]
    [:&.g-hover--rank-2
     {:background (important c/color-lightable--rank-2)}]
    [:&.g-focus--rank-1
     {:background c/color-lightable--rank-1}]
    [:&.g-focus--rank-2
     {:background c/color-lightable--rank-2}]))

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

(defn glowing-link [base-class base-text-color]
  (let [tc-inst      (tc base-text-color)
        sel-base     (str "." base-class)
        color-reg    (.toHslString tc-inst)
        color-hover  (.toHslString (.lighten tc-inst 10))
        color-active (.toHslString (.lighten tc-inst 20))]
    (list
      [sel-base
       (str sel-base ":visited")
       {:cursor     :pointer
        :background :none
        :stroke     color-reg
        :fill       color-reg
        :color      color-reg}]
      [(str sel-base ":hover")
       {:color     color-hover
        :fill      color-hover
        :stroke    color-hover}]
      [(str sel-base "--active")
       (str sel-base "--active:visited")
       (str sel-base ".g-active")
       {:color       color-active
        :stroke      color-active
        :fill        color-active
        :text-shadow (str "0 0 1px " color-active)}])))

(defn dark-link [base-class base-text-color]
  (let [tc-inst      (tc base-text-color)
        sel-base     (str "." base-class)
        color-reg    (.toHslString tc-inst)
        color-hover  (.toHslString (.darken tc-inst 10))
        color-active (.toHslString (.darken tc-inst 20))]
    (list
      [sel-base
       (str sel-base ":visited")
       {:cursor     :pointer
        :background :none
        :stroke     color-reg
        :fill       color-reg
        :color      color-reg}]
      [(str sel-base ":hover")
       {:stroke color-reg
        :fill   color-reg
        :color  color-hover}]
      [(str sel-base "--active")
       (str sel-base "--active:visited")
       (str sel-base ".g-active")
       {:color color-active
        :stroke color-active
        :fill color-active
        :box-shadow :none
        :font-weight 600}])))

(defn calc-link-css [base-class base-text-color]
  (let [tc-inst  (tc base-text-color)
        text-lum (.getLuminance tc-inst)]
    (garden/css
      (if (> text-lum 0.5)
        (glowing-link base-class base-text-color)
        (dark-link base-class base-text-color)))))

(def pane--opaque
  (merge pane-frame
    {:background c/color-lightable--opaque}))

(def placeholded
  [["&:empty::before"
    {:content "attr(placeholder)"
     :color c/color-text--placeholders}]
   ["&:focus:empty::before"
    {:content ""}]])
