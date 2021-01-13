(ns space-ui.pulsating-frames-2
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

(defn dim [dim-expr]
  (if (number? dim-expr)
    (px dim-expr)
    dim-expr))

(defn box-shadow [dim-radius color]
  (str
    (str "0 0 " (dim dim-radius) " " (name color))
    ", "
    (str "inset 0 0 " (dim dim-radius) " " (name color))))


(defn- border-brightness []
  (let [key-stops            [0    20   40 45 51   100]
        top-pos-values       [6    2    0  0  2    6]
        color-alpha-values   [0.2  0.5  1  1  0.5  0.2]
        shadow-radius-values [0    2    5  5  2    0]
        packs (map vector key-stops top-pos-values color-alpha-values shadow-radius-values)]
    (apply gs/at-keyframes
      (cons :border-brightness
        (for [[stop-perc top color-alpha shadow-radius] packs]
            [(pc stop-perc)
             {:border-color (hsla 0 0 100 color-alpha)
              :top          (px top)
              :box-shadow   (box-shadow shadow-radius :white)}])))))


(defn- style
  [frame-count animation-time-ms]
  (let [persp-step 19
        unit-time (/ animation-time-ms frame-count)]
    [:style
     (garden/css
       p-osc-frame-init
       (border-brightness)
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
                        :iteration-count :infinite}
            #:animation{:name :top
                        :duration animation-time-ms
                        :delay (* unit-time i)
                        :direction :forwards
                        :iteration-count :infinite})])

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

