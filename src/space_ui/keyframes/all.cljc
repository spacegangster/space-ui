(ns space-ui.keyframes.all
  (:require [garden.stylesheet :as gs]))

(def completion--blue
  (gs/at-keyframes :keyframes/completion
     [:0% {:width 0}]
     [:50%
      {:width      :100%
       :background "hsla(200, 80, 70, .6)"}]
     [:100%
      {:width      :100%
       :background "hsla(200, 50, 50, .2)"}]))

(def text-pulse-bw-imp
  (gs/at-keyframes :keyframes/text-pulse-bw-imp
    [:0%
     {:color "white !important"}]
    [:100%
     {:color "black !important"}]))

(def text-pulse-bw
  (gs/at-keyframes :keyframes/text-pulse-bw
    [:0%
     {:color "white"}]
    [:100%
     {:color "black"}]))



(defn oscillating-opacity
  ":space-ui/min – minimal value for opacity, so this will be the *most* transparent state
   :space-ui/max – max value for opacity, so this will be the *least* transparent state"
  [{:space-ui/keys [min max]
    :or {min 0.5, max 0.99}}]
  (gs/at-keyframes :keyframes/oscillating-opacity
    [:0%
     {:opacity min}]
    [:100%
     {:opacity max}]))

(defn mk-bg-pulse
  "pulsing background"
  [{:space-ui/keys [name, color1, color2]}]
  (let [color0 "hsla(0, 0%, 0%, 0)"]
    (gs/at-keyframes
      name
      [:0%
       {:background color0}]
      [:10%
       {:background color1}]
      [:100%
       {:background color2}])))


(defn mk-glow-pulse
  [{:space-ui/keys [name, color1, color2]}]
  (let [color0 "hsla(0, 0%, 0%, 0)"]
    (gs/at-keyframes
      name
      [:0%
       {:box-shadow (str "inset -2px -2px 0 0 " color0)}]
      [:10%
       {:box-shadow (str "inset -2px -2px 0 0 " color1)}]
      [:100%
       {:box-shadow (str "inset -2px -2px 0 0 " color2)}])))


(defn mk-glow-pulse--bottom
  [{:space-ui/keys [name, color1, color2]}]
  (let [color0 "hsla(0, 0%, 0%, 0)"]; (fade-out color1, 100)
    (gs/at-keyframes
      name
      [:0%
       {:box-shadow (str "inset 0px -2px 0 0 " color0)}]
      [:10%
       {:box-shadow (str "inset 0px -2px 0 0 " color1)}]
      [:100%
       {:box-shadow (str "inset 0px -2px 0 0 " color2)}])))


(def glow-pulse-bottom--red
 (mk-glow-pulse--bottom
   #:space-ui{:name   :keyframes/glow-pulse-bottom--red
              :color1 "hsla(30, 50%, 50%, 0.3)"
              :color2 "hsla(30, 50%, 50%, 0.9)"}))

(def glow-pulse-bottom--yellow
  (mk-glow-pulse--bottom
    #:space-ui{:name   :keyframes/glow-pulse-bottom--yellow
               :color1 "hsla(90, 50%, 50%, 0.3)"
               :color2 "hsla(90, 50%, 50%, 0.9)"}))

(def glow-pulse-bottom--green
  (mk-glow-pulse--bottom
    #:space-ui{:name   :keyframes/glow-pulse-bottom--green
               :color1 "hsla(120, 50%, 50%, 0.3)"
               :color2 "hsla(120, 50%, 50%, 0.9)"}))

(def bg--green-out
  (gs/at-keyframes
    :keyframes/bg--green-out
    [:0%
     {:background "hsla(100, 50%, 75%, 1.0)"}]
    [:100%
     {:background "hsla(100, 50%, 75%, 0.01)"}]))

(def bg-pulse--green
  (mk-bg-pulse
    #:space-ui{:name   :keyframes/bg-pulse--green
               :color1 "hsla(100, 50%, 75%, 0.0)"
               :color2 "hsla(100, 50%, 75%, 0.9)"}))

(def glow-pulse--red
  (mk-glow-pulse
    #:space-ui{:name   :keyframes/glow-pulse--red
               :color1 "hsla(4, 80%, 70%, 0.0)"
               :color2 "hsla(4, 80%, 70%, 0.9)"}))

(def glow-pulse--yellow
  (mk-glow-pulse
    #:space-ui{:name   :keyframes/glow-pulse--yellow
               :color1 "hsla(48, 70%, 66%, 0.3)"
               :color2 "hsla(48, 70%, 66%, 0.7)"}))

(def glow-pulse--green
  (mk-glow-pulse
    #:space-ui{:name   :keyframes/glow-pulse--green
               :color1 "hsla(100, 50%, 75%, 0.0)"
               :color2 "hsla(100, 50%, 75%, 0.9)"}))


(def all
  (list
    glow-pulse-bottom--red
    glow-pulse-bottom--yellow
    glow-pulse-bottom--green
    glow-pulse--red
    glow-pulse--yellow
    glow-pulse--green))

