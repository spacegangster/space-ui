(ns space-ui.style.mixins
  "Mixins api thing. Combines other mixins namespaces"
  (:require ["tinycolor2" :as tc]
            [garden.core :as garden]
            [space-ui.primitives :as sp]
            [space-ui.style.constants :as c]
            [space-ui.style.main-mixins :as mm]
            [space-ui.style.mixins-persistence-indication--glow :as mix.status--glow]
            [space-ui.style.constants :as sc]
            [space-ui.primitives :as prim]
            [common.functions :as f]))

(def display-centerflex mm/display-centerflex)
(def grid--flow-row mm/grid--flow-row)
(def display-flex-stretch mm/display-flex-stretch)
(def pane-frame mm/pane-frame)
(def important mm/important)
(def animation-glow-pulse--bottom mm/animation-pulse)
(def animation-glow-pulse--bottom--one mm/animation-pulse--one-fill)
(def animation-glow-pulse mm/animation-pulse)
(def animation-glow-pulse--one mm/animation-pulse--one-fill)

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

(def control--hoverable--fade-020
  (list
    {:cursor     :pointer
     :transition "color .1s ease-in-out"
     :opacity    0.2}
    [:&:hover
     {:opacity 1}]))

(def control--hoverable--fade-040
  (list
    {:cursor     :pointer
     :transition "color .1s ease-in-out"
     :opacity    0.4}
    [:&:hover
     {:opacity 1}]))

(def hint
  {:font-size sc/dim-fs-hint
   :font-weight 400
   :line-height 1.2
   :opacity 0.3})

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


(def ^{:doc ""} size #'mm/size)

;(alter-meta! #'size assoc :doc "ee")
;(meta #'size)
#_(let [m (meta #'mm/size)]
    (def ^{:doc (:doc m)
           :arglists (:arglists m)}
      size #'mm/size))


(def grid-area--all
  {:grid-area "1 / 1 / last-line / end"})

(defn focusable [& [color-bg-focused]]
  (let [color-bg-focused (or color-bg-focused sc/color-lightable--rank-1)]
   [:&.g-focus
    {:background color-bg-focused}]))


(def persistence-indicated--focus
  mix.status--glow/focus-mode)

(def persistence-indicated
  mix.status--glow/box)

(def icon--danger mm/icon--danger)


(def dual-scroll
  (list
    [:&--scroll-native
     {:height     :100%
      :overflow-x :hidden
      :overflow-y :scroll}]
    [:&--scroll-gemini
     {:height :100%}]))


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
    (into (list own-rules) mm/pane)))

(def filler--transparent
  (let [own-rules
        {:height          "calc(50vh - 112px)"
         :display         :flex
         :align-items     :center
         :line-height     :1.6
         :justify-content :center
         :background      :none
         :padding         (str "0 " sc/dim-item-side-pad)
         :color           sc/color-text--placeholders}]
    (into (list own-rules) mm/pane)))

(def nav-item mm/nav-item)


(defn glowing-link
  [{:as opts
    :comp/keys
    [base-class
     color-base
     props
     base-alpha--glow
     hover-state?
     important?]
    :or {base-alpha--glow 0.44}}]
  (let [tc-inst      (tc color-base)
        sel-base     (str "." base-class)
        props-n      (count props)
        ;
        reg-alpha    (or base-alpha--glow 0.44)
        hover-parent-alpha    (* 1.2 base-alpha--glow)
        hover-alpha    (* 1.5 base-alpha--glow)
        ;
        color-reg    (-> tc-inst ^js .clone (^js .setAlpha reg-alpha) ^js .toHslString)
        color-reg--opacit    (-> tc-inst ^js .clone (^js .setAlpha 0) ^js .toHslString)
        color-hover--by-parent  (-> tc-inst ^js .clone (^js .lighten 10) (^js .setAlpha hover-parent-alpha) ^js .toHslString)
        color-hover  (-> tc-inst ^js .clone (^js .lighten 10) (^js .setAlpha hover-alpha) ^js .toHslString)
        color-active (-> tc-inst ^js .clone (^js .lighten 20) (^js .setAlpha 1) ^js .toHslString)]
    (list
      [sel-base
       (str sel-base ":visited")
       {:cursor     :pointer
        :background :none
        :text-shadow (str "0 0 0px " color-reg--opacit)
        :transition "all .2s ease-in-out"
        :will-change "color stroke fill text-shadow"}
       (zipmap props (repeat props-n (str color-reg (if important? " !important"))))]


      (if hover-state?
        [(str ".g-hover > " sel-base)
         (str ".g-hover > * > " sel-base)
         (zipmap props (repeat props-n color-hover))])

      (if hover-state?
        [(str sel-base ":hover")
         (zipmap props (repeat props-n color-hover))])

      [(str sel-base "--active")
       (str sel-base "--active:visited")
       (str sel-base ".g-active")
       (zipmap props (repeat props-n color-active))
       {:text-shadow (str "0 0 1px " color-active)}])))



(defn dark-link
  [{:comp/keys
    [base-class
     color-base
     hover-state?
     props
     important?]}]
  (let [tc-inst      (tc color-base)
        props-n      (count props)
        sel-base     (str "." base-class)
        color-reg    (.toHslString tc-inst)
        color-hover  (.toHslString (.darken tc-inst 10))
        color-active (.toHslString (.darken tc-inst 20))]
    (list
      [sel-base
       (str sel-base ":visited")
       {:cursor     :pointer
        :background :none}
       (zipmap props (repeat props-n color-reg))]

      (if hover-state?
        [(str sel-base ":hover")
         (zipmap props (repeat props-n color-hover))])
      [(str sel-base "--active")
       (str sel-base "--active:visited")
       (str sel-base ".g-active")
       (zipmap props (repeat props-n color-active))
       {:box-shadow :none
        :font-weight 600}])))


(defn calc-link-rules
  [{:comp/keys
    [base-class
     color-base
     base-alpha--glow
     hover-state?
     important?]
    :as opts}]
  (let [tc-inst  (tc color-base)
        text-lum (.getLuminance tc-inst)]
    (if (> text-lum 0.5)
      (glowing-link opts)
      (dark-link opts))))

(def pane--opaque
  (merge pane-frame
    {:background c/color-lightable--opaque}))

(def placeholded
  [["&:empty::before"
    {:content "attr(placeholder)"
     :color c/color-text--placeholders}]
   ["&:focus:empty::before"
    {:content ""}]])
