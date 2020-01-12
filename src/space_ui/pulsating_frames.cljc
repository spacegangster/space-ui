(ns space-ui.pulsating-frames
  "Array of frames arranged on Z axis with pulse going through Z."
  (:require [garden.core :as garden]
            [space-ui.primitives :refer :all]
            [garden.stylesheet :as gs]))


(def p-osc-frame-init
  (gs/at-keyframes
    :p-osc-frame-init
    [:0%
     {:transform "scale(.95)"
      :opacity 0.1}]
    [:100%
     {:transform "scale(1)"
      :opacity  1}]))

(def col-border--off (hsla 0 0 100 0.2))
(def col-border--on-1 (hsla 0 0 100 0.5))
(def col-border--on-2 (hsla 0 0 100 1))


(defn- border-brightness []
  (gs/at-keyframes
    :border-brightness
    [:0%
     {:border-color col-border--off}]
    [:20%
     {:border-color col-border--on-1}]
    [:40%
     {:border-color col-border--on-2}]
    [:45%
     {:border-color col-border--on-2}]
    [:51%
     {:border-color col-border--on-1}]
    [:100%
     {:border-color col-border--off}]))

(defn- top []
  (gs/at-keyframes
    :top
    [:0%
     {:top :6px}]
    [:20%
     {:top :3px}]
    [:40%
     {:top :0px}]
    [:45%
     {:top :0px}]
    [:51%
     {:top :3px}]
    [:100%
     {:top :6px}]))


(defn- style
  [frame-count animation-time-ms]
  (let [persp-step 19
        unit-time (/ animation-time-ms frame-count)]
    [:style
     (garden/css
       p-osc-frame-init
       (border-brightness)
       (top)
       [:.p-osc-frame
        {:height :100%
         :width  :100%
         :position :absolute
         :left 0
         :top 0
         :border-radius :5px
         :border-width (px 5)
         :border-style :solid
         :border-color col-border--off}]

       (for [i (range 1 (inc frame-count))]
         [(str ".p-osc-frame--" i)
          {:border-width (px (Math/pow 1.5 2.0))}
          (transforms
            (translate-z (* -1 (Math/pow (* i 10) 1) persp-step)))
          (animations
            #:animation{:name :border-brightness
                        :duration animation-time-ms
                        :delay (* unit-time i)
                        :direction :forwards
                        :repeat :infinite}
            #:animation{:name :top
                        :duration animation-time-ms
                        :delay (* unit-time i)
                        :direction :forwards
                        :repeat :infinite})])

       [:.p-osc
        {:width :100%
         :height :100%
         :transform-style :preserve-3d
         :perspective :1000px}])]))


(defn frame [i]
  [:div.p-osc-frame
   {:class (str "p-osc-frame--" (inc i))}])

(defn root [{::keys [frame-count
                     animation-time-ms]
             :or {frame-count       8
                  animation-time-ms 1000}}]
  [:div.p-osc
   (style frame-count animation-time-ms)
   (for [i (range frame-count)]
     (frame i))])

