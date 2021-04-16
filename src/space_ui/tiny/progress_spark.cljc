(ns space-ui.tiny.progress-spark
  "Please don't change this, it's used in Lightpad"
  (:require [garden.stylesheet :as gs]
            [space-ui.primitives :as prim]))


;; Golden scheme
(def c-prog-main        (prim/hsla 30 50 87 0.1))
(def c-prog-track       (prim/hsl 30 50 87))
(def c-prog-active      (prim/hsl 30 50 91))

;; Blue scheme (dark)
(def c-prog-main-d        (prim/hsla 200 90 37 0.1))
(def c-prog-track-d       (prim/hsl  200 90 37))
(def c-prog-active-d      (prim/hsl  200 90 32))


(def style-rules
  (list
    (gs/at-keyframes :burn
      [:0% {:box-shadow (str "0 0 0px " c-prog-active)}]
      [:100% {:box-shadow (str "0 0 9px 1px " c-prog-active)}])
    (gs/at-keyframes :burn--darkk
      [:0% {:box-shadow (str "0 0 0px " c-prog-active-d)}]
      [:100% {:box-shadow (str "0 0 9px 1px " c-prog-active-d)}])

    [:.progress
     {:height      :2px
      :display     :grid
      :place-items "center start"
      :width       :100%
      :background  c-prog-main}
     [:&--dark
      {:height :3px}
      {:background c-prog-main-d}]

     [:&__track
      {:height      :2px
       :display     :grid
       :place-items "center end"
       :transition  ".4s width ease-in-out"
       :will-change :width
       :background  c-prog-track}
      [:&--dark
       {:height :3px}
       {:background c-prog-track-d}]]

     [:&__head
      {:width      :4px
       :height     :4px
       :position   :relative
       :top        :-1px
       :background c-prog-active
       :animation  "burn 0.05s ease-in-out infinite alternate"}
      [:&--dark
       {:background c-prog-active-d}
       {:animation "burn--dark 0.05s ease-in-out infinite alternate"}]]]))


(defn face--dark [prog-perc]
  [:div.progress.progress--dark
   [:div.progress__track.progress__track--dark
    {:style #?(:cljs {:width (str prog-perc "%")}
               :clj (str "width: " prog-perc "%"))}
    [:div.progress__head.progress__head--dark]]])

(defn face [prog-perc]
  [:div.progress
   [:div.progress__track
    {:style #?(:cljs {:width (str prog-perc "%")}
               :clj (str "width: " prog-perc "%"))}
    [:div.progress__head]]])

