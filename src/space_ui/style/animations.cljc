(ns space-ui.style.animations
  "CSS animation property groups")


(defn pulse
  "constant pulse"
  [keyframes-name]
  {:animation-duration        :2s
   :animation-timing-function :ease-in-out
   :animation-name            keyframes-name
   :animation-iteration-count :infinite
   :animation-direction       :alternate
   :animation-fill-mode       :forwards})

(defn one-fill
  "this animation stays after play"
  [keyframes-name & [duration-ms]]
  (let [duration (cond (not duration-ms) :1000ms
                       (number? duration-ms) (str duration-ms "ms")
                       :else duration-ms)]
    {:animation-duration        duration
     :animation-timing-function :ease-in-out
     :animation-name            keyframes-name
     :animation-iteration-count 1
     :animation-fill-mode       :forwards}))

(defn pulse--one-out
  "this animation fades in and out"
  [keyframes-name]
  {:animation-duration         :1400ms
   :animation-timing-function  :ease-in-out
   :animation-name             keyframes-name
   :animation-iteration-count  2
   :animation-direction        :alternate})


(defn animation-oscillation-opacity []
  {:animation "1s ease-in-out :keyframes/oscillating-opacity infinite alternate"})


