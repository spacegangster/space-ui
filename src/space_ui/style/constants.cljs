(ns space-ui.style.constants
  (:require [clojure.string :as s]))



;;;;; Colors

(def color-text--placeholders "hsl(0, 0%, 40%)")
(def color-lightable--filler  "hsla(0, 20%, 94%, .5)")
(def color-lightable--base    "hsla(0, 20%, 94%, .7)")
(def color-lightable--rank-2  "hsla(0, 37%, 94%, .81)")
(def color-lightable--rank-1  "hsla(0, 40%, 94%, .9)")
(def color-lightable--opaque    "hsl(0, 20%, 94%)")

(def color-focus--plane   "hsla(0, 40, 97, .8)")
(def color-focus--plane2  "hsla(0, 50, 97, .99)")

(def color-text           "hsl(0, 0, 30)")

(def color-icon--default "hsl(0, 0, 60)")
(def color-icon--hovered "hsl(0, 0, 45)")
(def color-icon--active  "hsl(0, 0, 30)")

(def color-control--default "hsla(0, 0%, 30%, .7)")
(def color-control--textual-default "hsla(220, 20, 40, .8)")
(def color-control--textual-hovered "hsla(220, 20, 40, 1)")
(def color-control--hovered "hsla(210, 50, 50, .7)")
(def color-control--active "hsla(210, 50, 50, 1)")

; focus modes colors
(def color-fm--clean-light-dimmer     "hsla(0, 0, 100, 1.0)")
(def color-fm--clean-dark-dimmer      "hsla(0, 0,   0, 1.0)")
(def color-fm--color-light-dimmer     "hsla(0, 0, 100, 0.1)")
(def color-fm--color-dark-dimmer      "hsla(0, 0,   0, 0.3)")
(def color-fm--glowing-letters-dimmer "hsla(0, 0,   0, 0.6)")

(def color-fm--clean-light-text     "hsl(0, 0, 40)")
(def color-fm--clean-dark-text      "hsl(0, 0, 65)")
(def color-fm--color-light-text     "hsl(0, 0, 25)")
(def color-fm--color-dark-text      "hsl(0, 0, 90)")
(def color-fm--glowing-letters-text "hsl(30, 20, 85)")


#_(def color-text          "hsl(220, 0, 60)")
#_(def color-day           color-text)
(def color-day--holiday  "hsl(20, 70, 60)")

(def col-task-bg            "hsla(0,0,100, .8)")
(def color-task-bg--focused "hsla(0,0,100, 1)")
(def col-task-fg            "hsl(0,0,30)")
(def col-task-bg--complete  "hsla(90,30,80, .5)")
(def col-task-fg--complete  "hsl(0,0,50)")



;;;;; dimensions

(defn px [s]
  (str s "px"))

(defn perc [s]
  (str s "%"))

(def dim-step 8) ; px
(def dim-step-px (px dim-step)) ; px

(defn dim-step-x
  ([] (dim-step-x 1))
  ([r]
   (px (* 8 r))))


(def dim-grid-halfstep "step / 2")
(def dim-grid-header-height "(dim-grid-step * 6)")
(def dim-grid-footer-height "(dim-grid-step * 6)")
(def dim-grid-footer-height--mobile "(dim-grid-step * 7)")

(def dim-interpane-gap  "step / 2")
(def dim-interitem-gap  "dim-interpane-gap / 2")
(def dim-item-side-pad  "step * 2")
(def dim-bottom-reading-space "33vh")
(def dim-root-pad--mobile "dim-grid-halfstep")
(def dim-root-pad-bottom--mobile "dim-grid-step * 0.5")

(def dim-fs-control--default "13.5px")
(def dim-fs-content--focus-mode "18px")

(def dim-entry-side-pad "step * 2")
(def dim-entry-side-pad--mobile "step * 1")
(def dim-note-grid-upper-section "step * 3")
(def dim-note-grid-lower-section "step * 5")
(def dim-note-grid-left-section  "dim-entry-side-pad")
(def dim-note-grid-right-section "dim-entry-side-pad")

(def dim-spacing--outmost-pad "dim-grid-halfstep")

(def dim-w-root__main "dim-grid-step * 120")
(def dim-w-root__main--slim "dim-grid-step * 96")

(def dim-w-central__branches "34%")
(def dim-w-central__content "66%")

(def dim-bp-laptop  "1400px")
(def dim-bp-slim    "1100px")
(def dim-bp-mobile  " 768px")
(def dim-bp-phones  " 500px")



;;;;; z indices

(def z-root-pane--content "10")
(def z-main-menu--hidden "0")
(def z-main-menu--desktop "11")
(def z-main-menu--open "20")
(def z-mobile-menu-btn "22")
(def z-layers-1 "50")
(def z-layers-exit "51")
(def z-root-status "52")
(def z-central-header "1")
(def z-central-tf-branches "30")
(def z-central-tabs-burger "1")
(def z-root-content--branches "z-main-menu--desktop + 1")
