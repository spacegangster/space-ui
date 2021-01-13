(ns space-ui.primitives
  (:require [clojure.string :as str]))

(def dim-step 8.0)
(defn hsl [h s l]
  (str "hsl(" h ", " s "%," l"%)"))
(defn hsla [h s l a]
  (str "hsla(" h ", " s "%," l"%, " a ")"))

(defn autopx [v]
  (cond-> v (number? v) (str "px")))

(defn map->hsl-string
  [{:keys [h s l a] :as m
    :or {h 0 s 0 l 0}}]
  (if a
    (str "hsla(" h ", " s "%, " l"%, " a ")")
    (str "hsl(" h ", " s "%, " l"%)")))

(assert (= "hsl(0, 0%, 100%)" (map->hsl-string {:l 100})))

(defn minmax [min max]
  (str "minmax(" (name min) ", " (name max) ")"))

(defn auto-hsl [v]
  (cond-> v (map? v) map->hsl-string))

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
  (str "translateZ(" (autopx px) ")"))

(defn translate-x [px]
  (str "translateX(" (autopx px) ")"))

(defn translate-y [px]
  (str "translateY(" (autopx px) ")"))

(defn square [d]
  {:width d
   :height d})

(defn scale [x]
  (str "scale(" x ")"))

(defn transforms [& exprs]
  {:transform (str/join " " exprs)})

(defn autoname [v]
  (cond-> v (keyword? v) name))
(assert (= "3px" (autoname :3px)))


:animation/name
:animation/duration
:animation/easing
:animation/delay
:animation/iteration-count

:animation/fill-mode
:animation.fill-mode/none
:animation.fill-mode/forwards
:animation.fill-mode/backwards
:animation.fill-mode/both

:animation/direction
:animation.direction/alternate-reverse
:animation.direction/alternate
:animation.direction/reverse
:animation.direction/normal

(defn animation
  "note duration must be an integer for milliseconds"
  [{:animation/keys
    [duration easing delay iteration-count
     direction fill-mode] :as opts}]
  (let [name' (some-> (:animation/name opts) name)
        expr [name'
              (some-> duration (str "ms"))
              (some-> easing autoname)
              (some-> delay (str "ms"))
              (some-> iteration-count autoname)
              (some-> direction autoname)
              (some-> fill-mode autoname)]]
    (str/join " " (filter some? expr))))

(defn animations [& animation-prms]
  {:animation (str/join ", " (map animation animation-prms))})


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


(def parse-float
  #?(:clj #(cond-> % (string? %) (Double/parseDouble)) :cljs js/parseFloat))


(defn autostep [s]
  (let [s1 (autoname s)
        s2 (and (string? s1) (not-empty s1))
        matches (and s2 (re-matches #"^(\d+\.?\d*)(step)$" s2))]
    (if-not matches
      s
      (let [x (parse-float (second matches))
            pixels (int (* (parse-float dim-step) x))]
        (str pixels "px")))))
(assert (= "24px" (autostep :3step)))
(assert (= "24px" (autostep "3step")))
(assert (= "26px" (autostep "3.25step")))


(defn grid-template
  "Produces grid-template string"
  [& args]
  (let [[rows cols] ((juxt butlast last) args)
        -row-fn (fn [[areas-str row-dim]]
                  (str "'" areas-str "' " (autopx (autoname row-dim))))]
    (str
      (str/join "\n" (mapv -row-fn rows))
      "\n"
      "\n / "
      (str/join " " (mapv (comp autoname autopx autostep) cols)))))

(assert (= "'a b c' \n\n / 3px 4px 4px"
           (grid-template
             ["a b c"]
             [:3px 4 4])))

(assert (= "'a b c' 3px\n'a b c' 2px\n\n / 3px 4px 4px"
           (grid-template
             ["a b c" :3px]
             ["a b c" 2]
             [:3px 4 4])))

(defn grid [{:grid/keys [gap place-items template]}]
  {:display       :grid
   :grid-gap      gap
   :place-items   place-items
   :grid-template (apply grid-template template)})
