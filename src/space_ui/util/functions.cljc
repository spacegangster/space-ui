(ns space-ui.util.functions
  (:require [clojure.string :as str]))

(defn assign
  "Primitive and faster left merge"
  [m1 m2]
  (persistent!
    (reduce-kv
      (fn [m k v] (assoc! m k v))
      (transient (or m1 {}))
      m2)))

(defn kw->str [kw]
  (if (nil? kw)
    "nil"
    (if-let [ns (namespace kw)]
      (str ns "/" (name kw))
      (name kw))))

(defn str->kw-or-nil [s]
  (if (= "nil" s)
    nil
    (keyword s)))


(defn options->transform [options]
  #?(:cljs
     (let [fov (:option/value (first options))]
       (cond
         (keyword? fov) keyword
         (int? fov) js/parseInt
         :else identity))))

(defn options->format [options]
  (let [fov (:option/value (first options))]
    (cond
      (keyword? fov) kw->str
      :else str)))


(defn parse-int-or-nil [str]
  #?(:cljs
     (let [parse-res (js/parseInt str)]
       (if-not (js/isNaN parse-res)
         parse-res))))


(defn parse-int-csv [str]
  (vec (keep parse-int-or-nil (str/split str #","))))

#?(:cljs
   (assert (= [3 -32] (parse-int-csv "3, -32"))))


(defn format-csv [vec]
  (str/join "," vec))

(assert (= "1,2,3" (format-csv [1 2 3])))


