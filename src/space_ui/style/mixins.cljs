(ns space-ui.style.mixins
  (:require [clojure.string :as s]
            [space-ui.style.constants :as c]))

(def display-centerflex
  {:display :flex
   :align-items :center})

(def pane-frame
  {:border-radius :2px})

(defn important [& strings]
  (str (s/join " " strings) " !important"))

(def pane
  [(merge
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
    {:background c/color-lightable--rank-2}]])

(def pane--opaque
  (merge pane-frame
    {:background c/color-lightable--opaque}))

(def placeholded
  [["&:empty::before"
    {:content "attr(placeholder)"
     :color c/color-text--placeholders}]
   ["&:focus:empty::before"
    {:content ""}]])
