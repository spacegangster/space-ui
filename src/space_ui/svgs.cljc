(ns space-ui.svgs
  "This can render icons like
  [icon fat-burger] -> <div class=svg-icon><svg>...</svg></div>"
  (:require [space-ui.bem :as bem]
            [common.functions :as f]))


(def fat-burger
  [:svg {:unicode "&#xe607;" :viewBox "0 0 1000 1000" :xmlns "http://www.w3.org/2000/svg"}])


(defn vec-w-attrs
  [[n-name ?props & children :as v] params]
  (if (map? ?props)
    (into [n-name (f/assign ?props params)] children)
    (into [n-name params] (cons ?props children))))

(defn force-params [icon params]
  (clojure.walk/postwalk
    (fn [x]
      (cond-> x
              (and (vector? x) (#{:svg :path} (first x)))
              (vec-w-attrs params)))
    icon))

(comment
  (force-params (get icons :icons/star-filled) {:fill "gold"}))

(defn icon
  "Icon name can be both icon itself or its name"
  ([icon] (icon icon {}))
  ([icon
    {:icon/keys [size colorscheme? svg-params]
     :keys      [active? danger?] :as opts}]
   (let [active? (or active? (:icon/active? opts))
         danger? (or danger? (:icon/danger? opts))
         css-class (str (if colorscheme? "g-colorscheme-control-icon ")
                        (if active? "g-active ")
                        (bem/bem-str :svg-icon (if (keyword? icon) icon) opts))]
     [:div {:style (if size {:width size :height size})
            :class css-class}
      (cond-> icon
              svg-params (force-params svg-params))])))

;; todo better question sign for help
;; todo archive icon
;; todo restore icon
;; todo better comment icon

(defn icon2
  [{icon :icon/name
    :icon/keys
         [size title
          colorscheme? active? danger? on-click]
    :as  opts}]
  (let [css-class (str (if colorscheme? "g-colorscheme-control-icon ")
                       (if danger? "g-danger ")
                       (if active? "g-active ")
                       (bem/bem-str :svg-icon icon opts))]
    [:div {:style    (if size {:width size :height size})
           :on-click on-click
           :title    title
           :class    css-class}
     icon]))

