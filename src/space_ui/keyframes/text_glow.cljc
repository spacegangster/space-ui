(ns space-ui.keyframes.text-glow
  (:require [garden.stylesheet :as gs]
            [space-ui.style.constants :as sc]
            [space-ui.primitives :as prim]))


(def color-text--gold        (prim/hsl 20, 50, 91))
(def color-text--gold-faded  (prim/hsl 20, 50, 81))

(def -glow-text-state-off
  {:color color-text--gold-faded
   :text-shadow (str "0 0 0px " color-text--gold-faded)})

(def -glow-text-state-on
  {:color color-text--gold
   :text-shadow (str "0 0 4px " color-text--gold)})

(def kf-glow-oscillation
  (gs/at-keyframes
    :keyframes/glow-oscillation--text
    [(sc/perc 0) -glow-text-state-off]
    [(sc/perc 2) -glow-text-state-on]
    [(sc/perc 4) -glow-text-state-off]
    [(sc/perc 7) -glow-text-state-on]))
