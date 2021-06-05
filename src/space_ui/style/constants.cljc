(ns space-ui.style.constants
  (:require [space-ui.primitives :as prim]
            [clojure.string :as str]))



;;;;; Colors ;;;;;

(def ^:const color-text--placeholders (prim/hsl  0,  0, 40))
(def ^:const col-fg--secondary (prim/hsl  0,  0, 60))
(def ^:const color-root-menu          (prim/hsla 0, 20, 94, 0.93))



(def ^:const color:bg:input-on-pane:base
  "lightable input base"
  (prim/hsla 0, 20, 98, 0.2))

(def ^:const color:bg:input-on-pane:hover
  "lightable input base"
  (prim/hsla 0, 20, 98, 0.5))

(def ^:const color:bg:input-on-pane:focus
  "lightable input base"
  (prim/hsla 0, 20, 98, 1))


(def ^:const color:bg:input-on-pane--blue
  (prim/hsla 220, 78, 94, 0.4))

(def ^:const color:bg:input-on-pane--blue:focus
  (prim/hsla 220, 78, 94, 0.6))

(def ^:const color:border:input-on-pane
  (prim/hsl 220, 78, 89))
(def ^:const color:border:control-on-pane "hsl(0, 0%, 80%)")

(def color:bg:pane-on-light
  "on a light gradient pane loses it's light gaining properties"
  (prim/hsl 203, 78, 99))

; colors for focusable pane backgrounds
(def ^:const color-lightable--filler
  "Color for lightable pane filler"
  (prim/hsla 0, 20, 94, 0.5))

(def ^:const color-lightable--base
  "lightable pane base color"
  (prim/hsla 0, 20, 94, 0.7))

(def ^:const color-lightable--rank-2
  "lightable pane light focus"
  (prim/hsla 0, 37, 94, 0.81))

(def ^:const color-lightable--rank-1
  "lightable pane full focus color"
  (prim/hsla 0, 40, 94, 0.9))

(def ^:const color-lightable--opaque  (prim/hsl  0, 20, 94))


(def ^:const color-focus--plane   (prim/hsla 0, 40, 97, 0.8))
(def ^:const color-focus--plane2  (prim/hsla 0, 50, 97, 0.99))

(def ^:const color-text      (prim/hsl 0, 0, 30))
(def ^:const color-text--shy (prim/hsl 0, 0, 50))

(def ^:const color-icon--default (prim/hsl 0, 0, 60))
(def ^:const color-icon--hovered (prim/hsl 0, 0, 45))
(def ^:const color-icon--active  (prim/hsl 0, 0, 30))

(def ^:const color-control--default         (prim/hsla   0,  0, 30, 0.7))
(def ^:const color-control--textual-default (prim/hsla 220, 20, 40, 0.8))
(def ^:const color-control--textual-hovered (prim/hsla 220, 20, 40, 1))
(def ^:const color-control--hovered         (prim/hsla 210, 50, 50, 0.7))
(def ^:const color-control--active          (prim/hsla 210, 50, 50, 1))

; focus modes colors
(def ^:const color-fm--clean-light-dimmer     (prim/hsla 0, 0, 100, 1.0))
(def ^:const color-fm--clean-dark-dimmer      (prim/hsla 0, 0,   0, 1.0))
(def ^:const color-fm--color-light-dimmer     (prim/hsla 0, 0, 100, 0.1))
(def ^:const color-fm--color-dark-dimmer      (prim/hsla 0, 0,   0, 0.3))
(def ^:const color-fm--glowing-letters-dimmer (prim/hsla 0, 0,   0, 0.6))

(def ^:const color-fm--clean-light-text     (prim/hsl  0,  0, 40))
(def ^:const color-fm--clean-dark-text      (prim/hsl  0,  0, 65))
(def ^:const color-fm--color-light-text     (prim/hsl  0,  0, 25))
(def ^:const color-fm--color-dark-text      (prim/hsl  0,  0, 90))
(def ^:const color-fm--glowing-letters-text (prim/hsl 30, 20, 85))


(def ^:const col-task-bg              (prim/hsla  0  0  98 0.97))
(def ^:const col-task-bg--time-label  (prim/hsla  0  0  98 0.40))
(def ^:const col-task-bg--on-pane     (prim/hsla 90 30  80 0.5))
(def ^:const color-task-bg--focused   (prim/hsla  0  0 100 1))
(def ^:const col-task-fg              (prim/hsl   0  0  30))
(def ^:const col-task-bg--complete    (prim/hsla 90 30  80 0.5))
(def ^:const col-task-fg--complete    (prim/hsl   0  0  50))

; (def ff-logo "-apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif")
(def ff-logo "-apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif")
(def ff-logo--mac "-apple-system, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif")

(def ff-stack-serif
  "'Apple Garamond', 'Baskerville', 'Times New Roman', 'Droid Serif', 'Times', 'Source Serif Pro', serif")
(def ff-stack-mono
  "'SF Mono', 'Monaco', 'Inconsolata', 'Fira Mono', 'Droid Sans Mono', 'Source Code Pro', monospace")


(def ff-main
  (str "-apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI', "
       "Roboto, 'Helvetica Neue', Helvetica, Arial, sans-serif"))
(def ff-email ; used in mail-service
  (str "-apple-system, 'Segoe UI', 'Helvetica Neue', Helvetica, Roboto, Arial, sans-serif"))
(def ff-main--mac
  (str "-apple-system, 'Helvetica Neue', Helvetica, sans-serif"))


;;;;; dimensions

(defn px
  ([s] (str s "px"))
  ([s1 s2] (str s1 "px " s2 "px"))
  ([s1 s2 s3] (str s1 "px " s2 "px " s3 "px"))
  ([s1 s2 s3 s4] (str s1 "px " s2 "px " s3 "px " s4 "px"))
  ([s1 s2 s3 s4 & step-values]
   (str/join " " (map px (into [s1 s2 s3 s4] step-values)))))

(defn autopx [v]
  (cond-> v (number? v) (str "px")))

(defn perc [s]
  (str s "%"))

(def ^:const dim-step 8) ; px
(def ^:const dim-step-px (px dim-step)) ; px

(defn d-step-x [r]
  (* dim-step r))

(defn d-step-x-px
  ([] (d-step-x-px 1))
  ([r]
   (px (* dim-step r)))
  ([r1 r2]
   (str (px (* dim-step r1)) " "
        (px (* dim-step r2))))
  ([r1 r2 r3]
   (str (px (* dim-step r1)) " "
        (px (* dim-step r2)) " "
        (px (* dim-step r3))))
  ([r1 r2 r3 r4]
   (str (px (* dim-step r1)) " "
        (px (* dim-step r2)) " "
        (px (* dim-step r3)) " "
        (px (* dim-step r4))))
  ([r1 r2 r3 r4 & step-values]
   (str/join " " (cons (d-step-x-px r1 r2 r3 r4)
                       (map d-step-x-px step-values)))))

(defn dims [& vals]
  (str/join " " (mapv (comp autopx prim/autoname) vals)))

(defn min-max [min max]
  (str "minmax(" (prim/autoname min) ", " (prim/autoname max) ")"))

(def ^:const dim-grid-halfstep         (/ dim-step 2))
(def ^:const dim-grid-halfstep-px      (px dim-grid-halfstep))
(def ^:const dim-grid-header-height-px         (d-step-x-px 6))
(def ^:const dim-grid-footer-height-px         (d-step-x-px 6))
(def ^:const dim:grid:nav-item-height-px       (d-step-x-px 6))
(def ^:const dim-grid-footer-height--mobile-px (d-step-x-px 8))

(def ^:const dim-interpane-gap               (/ dim-step 2))
(def ^:const dim-interpane-gap-px            (px dim-interpane-gap))
(def ^:const dim-interitem-gap-px            (px (/ dim-interpane-gap 2)))
(def ^:const dim-item-side-pad               (d-step-x-px 2))
(def ^:const dim-bottom-reading-space        "33vh")
(def ^:const dim-root-pad--mobile-px         dim-grid-halfstep-px)
(def ^:const dim-root-pad-bottom--mobile-px  dim-grid-halfstep-px)

(def ^:const dim-fs-control--default    "13.5px")
(def ^:const dim-fs-control--larger     "16px")
(def ^:const dim-fs-content "16px")
(def ^:const dim-fs-task  dim-fs-content)
(def ^:const dim-fs-subtask  "14px")
(def ^:const dim-fs-btn--reg "14px")
(def ^:const dim-fs-content--focus-mode "18px")
(def ^:const dim-fs-title "32px")
(def ^:const dim-fs-control--secondary  "15px")
(def ^:const dim-fs-hint  "14px")

(def ^:const dim-entry-side-pad          "step x 2"  (d-step-x 2))
(def ^:const dim-entry-side-pad--mobile  "step"   (d-step-x 1))
(def ^:const dim-entry-side-pad--mobile-px   (px dim-entry-side-pad--mobile))
(def ^:const dim-note-grid-upper-section-px  (d-step-x-px 3))
(def ^:const dim-note-grid-lower-section-px  (d-step-x-px 5))
(def ^:const dim-note-grid-left-section-px   (px dim-entry-side-pad))
(def ^:const dim-note-grid-right-section  dim-entry-side-pad)
(def ^:const dim-note-grid-right-section-px  (px dim-note-grid-right-section))

(def ^:const dim-spacing--outmost-pad dim-grid-halfstep-px)

(def ^:const dim-w-root__main-px (d-step-x-px 120))
(def ^:const dim-w-root__main--slim-px (d-step-x-px 96))

(def ^:const dim-w-central__branches "34%")
(def ^:const dim-w-central__content "66%")

(def ^:const dim-bp-laptop     "1400"  1400)
(def ^:const dim-bp-laptop-px  "1400px" (px dim-bp-laptop))

(def ^:const dim-bp-wide-screen     "1600"  1600)
(def ^:const dim-bp-wide-screen-px  "1600px" (px dim-bp-wide-screen))

(def ^:const dim-bp-slim    1100)
(def ^:const dim-bp-slim-px
  "for narrow desktop around 1100px
   aspect ratio 12/10"
  (px dim-bp-slim))

(def ^:const dim-bp-slim-height-px
  "for narrow desktop around 1100px
   aspect ratio 12/10"
  (px (* dim-bp-slim (/ 10 12))))

(def ^:const dim-bp-mobile "768"  768)
(def ^:const dim-bp-mobile-px "768"  (px dim-bp-mobile))

(def ^:const dim-bp-ipad-width--landscape "1024"  1024)
(def ^:const dim-bp-ipad-width--landscape-px "1024px"  "1024px")
(def ^:const dim-bp-ipad-height--landscape "768"  768)
(def ^:const dim-bp-ipad-height--landscape-px "768px"  "768px")

(def ^:const dim-bp-phones  500)
(def ^:const dim-bp-phones-px "500"  (px dim-bp-phones))



(def ^:const mq-mobile-header-on
  "media queries when mobile header should be on (mobile mode)"
  [{:max-width dim-bp-slim-px}])

(def ^:const mq-mobile-header-off
  "media queries when mobile header should be on (mobile mode)"
  [{:min-width dim-bp-slim-px}])


(def ^:const mq-small-laptops
  "media queries for devices smaller than ipad (landscape mode)"
  [{:max-width dim-bp-laptop-px
    :min-width dim-bp-ipad-width--landscape-px
    :min-aspect-ratio "12 / 10"}])

(def ^:const mq:theme-light
  "media query for the light theme"
  {:prefers-color-scheme :color-schemes/light})

(def ^:const mq:theme-dark
  "media query for the dark theme"
  {:prefers-color-scheme :color-schemes/dark})

(def ^:const mq-smaller-than-ipad
  "media queries for devices smaller than ipad (landscape mode)"
  [{:max-width (px (dec dim-bp-ipad-width--landscape))}])

(def ^:const mq-smaller-than-ipad--portrait
  "media queries for devices smaller than ipad (landscape mode)"
  [{:max-width (px (dec dim-bp-mobile))}])

(def ^:const mq-phone-and-smaller
  "media queries for phones and smaller devices (max-width < 500)"
  [{:max-width dim-bp-phones-px}])

(def ^:const mq:smaller-than-iphone-x
  "media queries for phones and smaller devices (max-width < 500)"
  [{:max-width :374px}])

(def ^:const mq-narrow-portrait
  {:max-aspect-ratio "6/10"})

(def ^:const mq-larger-than-ipad
  "media queries for devices larger than ipad (landscape mode)"
  [{:min-width dim-bp-ipad-width--landscape-px}])

(def ^:const mq-larger-than-ipad--diag
  "media queries for devices smaller than ipad (landscape mode)"
  [{:min-width dim-bp-ipad-width--landscape-px
    :min-height dim-bp-ipad-height--landscape-px}])

(def ^:const mq-fullscreen-branches-on
  "media queries when fullscreen branches should be on (mobile mode)"
  [{:max-width dim-bp-mobile-px}])

(def ^:const mq-fullscreen-branches-off
  "media queries when fullscreen branches should be off (desktop mode)"
  [{:min-width (px (inc dim-bp-mobile))}])


(def ^:const mq-mobile-tabs-off
  "media queries when mobile tabs should be off, and side menu used"
  [{:min-width  (px dim-bp-ipad-width--landscape)}])
; :min-height (px (inc dim-bp-ipad-height--landscape))

(def ^:const mq-mobile-tabs-on
  [{:max-width  (px (dec dim-bp-ipad-width--landscape))}
   #_{:min-width  (px dim-bp-ipad-width--landscape)
      :max-aspect-ratio "11/10"}])



(def ^:const mq-proper-laptop+widescreen
  [{:min-width dim-bp-wide-screen-px}
   {:min-width        dim-bp-laptop-px
    :min-aspect-ratio "14 / 10"}])



;;;;; z indices

(def ^:const z-root-pane--content     10)
(def ^:const z-main-menu--hidden       0)
(def ^:const z-main-menu--desktop     11)
(def ^:const z-main-menu--open        20)
(def ^:const z-mobile-menu-btn        22)
(def ^:const z-layers-1               50)
(def ^:const z-layers-exit            51)
(def ^:const z-root-status            52)

(def ^:const z-central-entities        1)
(def ^:const z-central-tf-branches    30)
(def ^:const z-central-header         31)
(def ^:const z-central-tf-tabs        31)
(def ^:const z-central-tabs-mob-menu  33)
(def ^:const z-central-tabs-burger--open    (inc z-central-tabs-mob-menu))
(def ^:const z-central-day-plan        2)
(def ^:const z-central-day-plan--active (inc z-central-tf-tabs))

(def ^:const z-slide                  (inc z-central-tf-tabs))
(def ^:const z-task-focused           33)
(def ^:const z-task-time-input-active 34)
(def ^:const z-tf-scale               32)
(def ^:const z-root-content--branches (inc z-main-menu--desktop))
