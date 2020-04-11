(ns space-ui.tiny.status-square-3
  (:require [space-ui.primitives :as prim]))

(def c-task-fg-main     (prim/hsl 20 50 95))
(def c-task-fg-complete (prim/hsla 20 50 95, 0.5))

(def style-rules
  (let [w :10px]
    [:.sq-status-2
     {:width  w
      :height w}
     [:&--stuck
      {:border "1px dashed white"}]
     [:&--idle
      {:border "1px solid white"}]
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
  [:div.sq-status-2 (if status {:class (str "sq-status-2--" status)})])
