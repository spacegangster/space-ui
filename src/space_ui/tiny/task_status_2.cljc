(ns space-ui.tiny.task-status-2
  (:require [space-ui.primitives :as prim]))

(def c-task-fg-main     (prim/hsl 20 50 95))
(def c-task-fg-complete (prim/hsla 20 50 95, 0.5))

(def style-rules
  (let [w :10px]
    [:.sq-status
     {:width  0
      :height :0px
      :border "0px solid white"}
     [:&--in-progress
      {:background c-task-fg-main
       :width  w
       :height w}]
     [:&--complete
      {:background c-task-fg-complete
       :width  w
       :height w}]]))

(defn face
  "status ^String"
  [status]
  [:div.sq-status (if status {:class (str "sq-status--" status)})])
