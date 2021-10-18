(ns space-ui.util.functions)


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

