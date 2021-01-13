(ns space-ui.style.mixins-persistence-indication--glow
  "Indicate persistence with different glow (box-shadow)"
  (:require [space-ui.style.animations :as animations]))


(def box
  (list
    [:&--valid
     (animations/pulse--one-out :keyframes/glow-pulse--green)]
    [:&--in-progress
     (animations/pulse :keyframes/glow-pulse--yellow)]
    [:&--error
     (animations/one-fill :keyframes/glow-pulse--red)]))

(defn focus-mode
  "persistence indication for focus mode"
  [sel]
  (list
    [(str sel "--valid")
     (animations/one-fill :keyframes/glow-pulse--green)]
    [(str sel "--in-progress")
     (animations/pulse :keyframes/glow-pulse--yellow)]
    [(str sel "--error")
     (animations/one-fill :keyframes/glow-pulse--red)]))
