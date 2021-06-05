(ns space-ui.util.dom
  (:require [clojure.string :as s]
            [cljs.reader :as reader]
            ["./set-caret-position" :refer [setCaretPosition]]))

(def window js/window)
(def doc js/document)
(def doc-el js/document.documentElement)
(def day-millis (* 86400 1000))
(def str->id js/parseInt)

(defn- gid [id]
  (.getElementById js/document id))

(defn get-body-width []
  (.-width (.getBoundingClientRect (.-body js/document))))

(def jsget goog.object/getValueByKeys)

(defn get-elem-pos [elem]
  (let [rect (.getBoundingClientRect elem)]
    {:width  (.-width rect)
     :height (.-height rect)
     :top    (.-top rect)
     :left   (.-left rect)}))

(defn elem-height [elem]
  (:height (get-elem-pos elem)))

(defn get-window-scroll []
  (or (.-pageYOffset window)
      (-> doc .-documentElement .-scrollTop)
      (-> doc .-body .-scrollTop)
      0))

(defn scroll-to! [scroll]
  (let [res-scroll-y (- scroll (get-window-scroll))]
    (.scrollBy window 0 res-scroll-y)))

(defn scroll-by! [scroll]
  (.scrollBy window 0 scroll))

(defn set-style! [^js/Element elem, style-map]
  (let [style-obj (^js .-style elem)
        set-style-attr! (fn [style-obj k v]
                          (aset style-obj k v)
                          style-obj)]
    (reduce-kv set-style-attr! style-obj style-map)))
(comment
  (set-style! js/window.temp2 {"height" 0, "overflow" "hidden"}))


(defn get-viewport-height []
  js/window.innerHeight)

(defn calc-evt-path-js [evt]
  (or (.-path evt)
      (and (.-composedPath evt)
           (.composedPath evt))))

(defn has-class? [elem class-name]
  (some-> elem (.-classList) (.contains class-name)))

(defn evt-has-in-path? [evt class-name]
  (let [path (-> evt (jsget "nativeEvent") calc-evt-path-js js->clj)]
    (some #(has-class? % class-name) path)))


(defn get-element-by-id [id]
  (js/document.getElementById id))

(defn parse-elem-eid [elem]
  (some-> elem (jsget "dataset" "entityId") str->id))

(defn evt->data-prop [^js react-evt prop]
  (some-> react-evt (jsget "currentTarget" "dataset" prop)))

(defn evt->entity-id [^js react-evt]
  (some-> (evt->data-prop react-evt "entityId") str->id))

(defn evt->data-idx [^js react-evt]
  (evt->data-prop react-evt "idx"))


(defn ^js/Element get-first-by-classname
  ([^js/String classname]
   (aget (js/document.getElementsByClassName classname) 0))
  ([^js/String classname, ^js/Element element]
   (aget (.getElementsByClassName element classname) 0)))


(defn select-one [sel & [ctx]]
  (.querySelector (or ctx js/document) sel))

(defn select-all [sel & [ctx]]
  (.querySelectorAll (or ctx js/document) sel))

(defn get-target-text [evt]
  (.. evt -currentTarget -textContent))

(defn get-target-value [evt]
  (.. evt -currentTarget -value))

(defn parse-data-id [elem]
  (if elem
    (js/parseInt (.. elem -dataset -id))))

(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))

(defn- camel-dash-replace [match]
  (str "-" (.toLowerCase (first match))))

(defn is-element? [x]
  (instance? js/HTMLElement x))

(defn closest-parent [elem css-class]
  (if-not elem
    nil
    (if (has-class? elem css-class)
      elem
      (recur (.-parentElement elem) css-class))))

(defn camel->dashes
  "Convert camelCase identifier string to hyphen-separated keyword."
  [id]
  (s/replace id #"[A-Z]" camel-dash-replace))

(defn dataset->clj-raw [ds]
  (let [keys (js->clj (.keys js/Object ds))]
    (persistent!
      (reduce (fn [mem key]
                (assoc! mem (keyword (camel->dashes key)) (aget ds key)))
              (transient {})
              keys))))

(defn parse-int-or-nil [v]
  (let [x (js/parseInt v)]
    (if (js/isNaN x) nil x)))

(defn parse-entity-id [elem]
  (parse-int-or-nil (jsget elem "dataset" "entityId")))

(defn parse-evt-entity-id [evt]
  (-> evt (jsget "currentTarget" "dataset" "entityId") parse-int-or-nil))

(defn try-parse-int [str]
  (let [parse-res (js/parseInt str)]
    (if (js/isNaN parse-res)
      str
      parse-res)))

(defn read-number-or-nil [str]
  (try
    (let [read-res (reader/read-string str)]
      (if (number? read-res)
        read-res
        nil))
    (catch js/Error e
      nil)))

(defn read-keyword-or-nil [str]
  (try
    (let [read-res (reader/read-string str)]
      (if (keyword? read-res)
        read-res
        str))
    (catch js/Error e
      str)))

(defn autoparse [str]
  (cond
    (#{"null" "nil"} str) nil
    (empty? str) nil
    :else
    (or
      (read-number-or-nil str)
      (read-keyword-or-nil str)
      str)))
;(autoparse "aaa:2323")



(defn set-caret-to-start! [el]
  (setCaretPosition el 0))

(defn set-caret-to-end! [el]
  (let [content (.-textContent el)
        cl (.-length content)]
    (setCaretPosition el cl)))


(defn focus-element [el & [^keyword caret-pos]]
  (when el
    (try
      (.click el)
      (catch js/Error e nil))
    (.focus el)
    (case caret-pos
      :caret/first (set-caret-to-start! el)
      :caret/last (set-caret-to-end! el)
      nil)))

(defn focus-selector [sel & [^keyword caret-pos]]
  (when-let [el (select-one sel)]
    (focus-element el caret-pos)))

(defn focus-id [id & [^keyword caret-pos]]
  (when-let [el (gid id)]
    (focus-element el caret-pos)))


(defn focus-selector--delayed
  ([sel]
   (focus-selector--delayed sel 50))
  ([sel timeout]
   (js/setTimeout #(focus-selector sel) timeout)))
