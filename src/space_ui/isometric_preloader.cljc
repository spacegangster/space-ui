(ns space-ui.isometric-preloader
  (:require [garden.core :as garden]
            [clojure.string :as s]
            [space-ui.primitives :refer :all]
            [garden.stylesheet :as gs]))


(def ray-fill--off
  {:background  (hsla 180 0 91 0.3)
   :box-shadow  (str "0 0 0px " (hsla 180 100 80 0.5)"}")})

(def ray-fill--on
  {:background (hsla 180 0 91 1.0)
   :box-shadow (str "0 0 6px " (hsla 180 100 81 0.9))})

(def circle-init
  (gs/at-keyframes
    :circle-init
    [:0%
     {:transform "scale(.95)"
      :opacity 0.1}]
    [:100%
     {:transform "scale(1)"
      :opacity  1}]))

(def ray-end-width-vw 1)
(def anim-amplitude 2)

(defn- stretch []
  (gs/at-keyframes
    :stretch
    [:0%
     {:width (vw ray-end-width-vw)}]
    [:19%
     {:width  (vw (* ray-end-width-vw anim-amplitude))}]
    [:38%
     {:width  (vw ray-end-width-vw)}]))

(defn- brightness []
  (gs/at-keyframes
    :brightness
    [:0%
     {:background "hsla(0, 0%, 100%, .8)"}]
    [:100%
     {:background "hsla(0, 0, 100%, 1)"}]))

(defn- degs-weird-n-curvy [ray-count ray-n]
  (let [z-deg (/ (* 360 ray-n) ray-count)
        z-rad (Math/toRadians z-deg)
        y-deg (* 45 (Math/abs (Math/cos z-rad)))
        x-deg (* 45 (Math/abs (Math/sin z-rad)))]
    [z-deg y-deg x-deg]))

(defn- degs-2 [ray-count ray-n]
  (let [z-deg (/ (* 360 ray-n) ray-count)
        z-rad (Math/toRadians z-deg)
        y-deg (* 45 (Math/cos z-rad))
        x-deg (* 45 (Math/sin z-rad))]
    [x-deg y-deg z-deg]))

(defn- degs-3 [ray-count ray-n]
  (let [z-deg (/ (* 360 ray-n) ray-count)
        z-rad (Math/toRadians z-deg)
        y-deg (* 45 (Math/abs (Math/cos z-rad)))
        x-deg (* 45 (Math/abs (Math/sin z-rad)))]
    [x-deg y-deg z-deg]))

(defn- degs [ray-count ray-n]
  (let [z-deg (/ (* 360 ray-n) ray-count)
        z-rad (Math/toRadians z-deg)
        y-deg (* 55 (Math/sin z-rad))
        x-deg (* 55 (Math/cos z-rad))]
    [x-deg y-deg z-deg]))

(defn- style [ray-count]
  [:style
   (garden/css
     circle-init
     (stretch)
     (brightness)
     [:.circle
      {:height :100%
       :width  :100%
       :position :absolute
       :top 0
       :animation "circle-init 300ms ease-in-out 1 forwards"}
      [:&__part
       {:position         :absolute
        :height           :1px
        :top              :50%
        :left             :50%
        :width            :50%
        :text-align       :right
        :transform-origin "0 0"}
       (for [i (range ray-count)]
         [(str "&--" (inc i))
          (let [[x-deg y-deg z-deg] (degs ray-count i)]
            (transforms
              (rotate-y y-deg)
              (rotate-x x-deg)
              (rotate-z z-deg)))])
       [:&__stretcher
        {:height :100%
         :width :100%
         :position :relative
         :will-change :width}
        [:&__ending
         {:position      :absolute
          :top           0
          :right         0
          :width         :20%
          :height        :6px
          :border-radius :5px}
         ray-fill--off
         (for [i (range ray-count)]
           [(str "&--" (inc i))
            (animations
              (animation
                {:animation/name :brightness
                 :animation/duration 150
                 :animation/delay (* 60 i)
                 :animation/easing :ease-in-out
                 :animation/repeat :infinite
                 :animation/direction :alternate}))])]]]])])


(defn root [{::keys [ray-count]
             :or {ray-count 90}}]
  [:div.circle
   (style ray-count)
   (for [i (range ray-count)]
    [:div.circle__part
     {:class (str "circle__part--" (inc i))}
     [:div.circle__part__stretcher
      {:class (str "circle__part__stretcher--" (inc i))}
      [:div.circle__part__stretcher__ending
       {:class (str "circle__part__stretcher__ending--" (inc i))}]]])])
