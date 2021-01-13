(ns space-ui.tiny.status-led--persistence
  "Green/yellow/red  led indicator"
  (:require [space-ui.style.main-mixins :as mm]
            [space-ui.style.animations :as animations]))


(def style-rules
  [:.status-led-p
   {:transition "all .2s ease-in-out"}
   (mm/size 6 8)

   [:&--error
    (mm/size 8 8)
    {:background "hsla(20, 100%, 50%, .9)"}]

   [:&--in-progress
    (mm/size 6 8)
    {:background "hsla(50, 100%, 50%, .8)"}]

   [:&--valid
    {:background "hsla(120, 83%, 60%, .5)"}
    (animations/one-fill :keyframes/bg--green-out 3000)]])


(defn face
  "Green/yellow/red  led indicator.

   line-status – keyword or string ending "
  [line-status title]
  [:div.status-led-p
   {:class (str "status-led-p--" (name line-status))
    :title title}])
