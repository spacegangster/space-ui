(ns space-ui.perspective-oscillator
  "This one is about depth of field.
  Array of frames is bouncing forwards and backwards."
  (:require [garden.core :as garden]
            [space-ui.primitives :refer :all]
            [garden.stylesheet :as gs]))


(defn- perspective []
  (gs/at-keyframes
    :perspective
    [:0%
     {:perspective :1px}]
    [:100%
     {:perspective :1000px}]))

(defn- style [frame-count]
  (let [persp-step 12]
    [:style
     (garden/css
       (perspective)
       [:.p-osc-frame
        {:height :100%
         :width  :100%
         :position :absolute
         :left 0
         :top 0
         :border-radius :2px
         :border "1px solid white"}]

       (for [i (range 1 (inc frame-count))]
         [(str ".p-osc-frame--" i)
          {:border (str (px (Math/pow 1.5 1.9)) " solid white")}
          (transforms
            (translate-z (* -1 (Math/pow (* i 10) 1) persp-step)))])

       [:.p-osc
        {:width :100%
         :height :100%
         :transform-style :preserve-3d
         :perspective :100px
         :animation
         (animation
           #:animation{:name :perspective
                       :duration 1800
                       :direction :alternate
                       :iteration-count :infinite
                       :easing :ease-in-out})}])]))

(defn frame [i]
  [:div.p-osc-frame
   {:class (str "p-osc-frame--" (inc i))}])

(defn root [{::keys [frame-count]
             :or {frame-count 8}}]
  [:div.p-osc
   (style frame-count)
   (for [i (range frame-count)]
     (frame i))])

