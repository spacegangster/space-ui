(ns space-ui.tiny.progress-spark
  (:require [garden.stylesheet :as gs]
            [space-ui.primitives :as prim]))


(def c-prog-main        (prim/hsla 30 50 87 0.1))
(def c-prog-track       (prim/hsl 30 50 87))
(def c-prog-active      (prim/hsl 30 50 91))

(def style-rules
  (list
    (gs/at-keyframes :burn
                  [:0%   {:box-shadow (str "0 0 0px "     c-prog-active)}]
                  [:100% {:box-shadow (str "0 0 9px 1px " c-prog-active)}])
    [:.progress
     {:height :2px
      :display :grid
      :place-items "center start"
      :width :100%
      :background c-prog-main}
     [:&__track
      {:height :2px
       :display :grid
       :place-items "center end"
       :background c-prog-track}]
     [:&__head
      {:width :4px
       :height :4px
       :position :relative
       :top :-1px
       :background c-prog-active
       :animation "burn 0.05s ease-in-out infinite alternate"}]]))

(defn face [prog-perc]
  [:div.progress
   [:div.progress__track
    {:style (str "width: " prog-perc "%")}
    [:div.progress__head]]])

