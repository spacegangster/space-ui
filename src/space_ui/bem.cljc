(ns space-ui.bem
  "Add modifiers to CSS classes as in BEM method.
   See rationale here: http://getbem.com/naming/

   For BEM with elements see: https://github.com/druids/ccn
   Compared to CCN this gist supports maps."
  (:require [clojure.string :as str]))



;; ns private fns ;;
(defn unfold-modifier-maps [possible-modifier-map]
  (if-not (map? possible-modifier-map)
    possible-modifier-map
    (mapv first (filterv second possible-modifier-map))))

(defn flatten-modifiers [modifiers]
  (flatten (mapv unfold-modifier-maps modifiers)))

(defn- bem-str-strict
  [css-class-name modifiers]
  (let [base-class-name (name css-class-name)
        modifiers (flatten-modifiers modifiers)
        -append
        (fn [final-class-name modifier-name]
          (if modifier-name
            (str final-class-name " " base-class-name "--" (str/replace (name modifier-name) "?" ""))
            final-class-name))]
    (reduce -append base-class-name modifiers)))



;; API ;;

(defn bem-str
  "Pass class name and any number of modifiers or vectors/maps of modifiers
   ^String/^Keyword css-class-name
   & modifiers

   Example
   (bem-str :bem :mod1 [:mod2 :mod3] {:mod4 false :mod5 1})
   => \"bem bem--mod1 bem--mod2 bem--mod3 bem--mod5\""
  [css-class-name & modifiers]
  (bem-str-strict css-class-name modifiers))

(assert (= "bem bem--mod1 bem--mod2 bem--mod3 bem--mod5"
           (bem-str :bem :mod1 [:mod2 :mod3] {:mod4 false :mod5 1})))

(defn bem
  "Returns a hash-map like {:class \"your-computed-class ...\"}
   See bem-str doc for details."
  ([css-class-name]
   {:class (cond-> css-class-name (keyword? css-class-name) (name))})
  ([css-class-name & modifiers]
   {:class (bem-str-strict css-class-name modifiers)}))

