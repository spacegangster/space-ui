(ns space-ui.primitives
  (:require [clojure.string :as s]))

(defn hsl [h s l]
  (str "hsl(" h ", " s "%," l"%)"))
(defn hsla [h s l a]
  (str "hsla(" h ", " s "%," l"%, " a ")"))

(defn vw [x] (str (float x) "vw"))
(defn vh [x] (str (float x) "vh"))
(defn px [x] (str (float x) "px"))
(defn em [x] (str (float x) "em"))
(defn pc [x] (str (float x) "%"))
(defn ms [x] (str (float x) "ms"))

(defn rotate-z [deg]
  (str "rotateZ(" deg "deg)"))
(defn rotate-x [deg]
  (str "rotateX(" deg "deg)"))
(defn rotate-y [deg]
  (str "rotateY(" deg "deg)"))

(defn translate-z [px]
  (str "translateZ(" px
       (if-not (string? px) "px")
       ")"))

(defn translate-x [px]
  (str "translateX(" px
       (if-not (string? px) "px")
       ")"))

(defn translate-y [px]
  (str "translateY(" px
       (if-not (string? px) "px")
       ")"))

(defn square [d]
  {:width d
   :height d})

(defn scale [x]
  (str "scale(" x ")"))

(defn transforms [& exprs]
  {:transform (s/join " " exprs)})

:animation/name
:animation/duration
:animation/easing
:animation/delay
:animation/repeat
:animation/direction

(defn animation [{:animation/keys [duration easing delay repeat direction] :as opts}]
  (let [name' (some-> (:animation/name opts) name)
        expr [name'
              (some-> duration (str "ms"))
              (some-> easing name)
              (some-> delay (str "ms"))
              (some-> repeat name)
              (some-> direction name)]]
    (s/join " " (filter some? expr))))

(defn animations [& animation-prms]
  {:animation (s/join ", " (map animation animation-prms))})


(defn autoname [v]
  (cond-> v (keyword? v) name))

(defn poly-coord [[x y]]
  (str (autoname x) " " (autoname y)))

(defn polygon [& coords]
  (str "polygon("
       (clojure.string/join ", " (map poly-coord (partition 2 coords)))
       ")"))

(defn poly-clip [& points]
  (let [poly (apply polygon points)]
    {:clip-path poly
     :-webkit-clip-path poly}))
