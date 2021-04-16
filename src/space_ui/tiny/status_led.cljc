(ns space-ui.tiny.status-led
  "Green/yellow/red  led indicator"
  (:require [space-ui.style.main-mixins :as mm]))


(def style-rules
  [:.status-led
   {:transition "all .2s ease-in-out"}
   [:&--fixed
    (mm/size :8px)]

   [:&--offline
    :&--error
    (mm/size :16px)
    {:background "hsla(20, 100%, 50%, .9)"}]

   [:&--in-progress
    :&--other
    :&--temporarily-unavailable
    (mm/size :12px)
    {:background "hsla(50, 100%, 50%, .8)"}]

   [:&--online
    :&--valid
    {:background "hsla(120, 83%, 60%, .5)"}]])


(defn face
  "Green/yellow/red  led indicator.

   line-status – keyword or string ending "
  [line-status title]
  [:div.status-led
   {:class (str "status-led--" (name line-status))
    :title title}])

(defn face--static
  "Green/yellow/red  led indicator.
   This face doesn't grow or shrink.

   line-status – keyword or string ending "
  [line-status title]
  [:div.status-led--fixed
   {:class (str "status-led--" (name line-status))
    :title title}])
