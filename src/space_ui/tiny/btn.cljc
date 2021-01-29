(ns space-ui.tiny.btn
  (:require [space-ui.bem :as bem]
            [space-ui.tiny.link]
            [space-ui.svgs :as svgs]))


(def on-click-prop #?(:cljs :on-click :clj :onclick))

(defn btn
  [{:btn/keys
    [icon css-class mods label on-click goal-id
     tooltip title tabindex type attrs]
    :as params}]
  [:button.btn
   (cond-> {:class        (str (bem/bem-str :btn mods) " " css-class)
            :title        (or title tooltip)
            on-click-prop (or on-click (space-ui.tiny.link/calc-reach-goal goal-id))
            :data-goal-id goal-id
            :tabindex     tabindex
            :type         type}
           attrs (merge attrs)
    (if icon
      [:div.btn__icon
       [svgs/icon icon]])
    (if (seq label)
      [:div.btn__label label]))])

(defn cta
  [{:btn/keys
    [icon css-class mods label on-click
     tooltip title tabindex type attrs]
    :as params}]
  (btn (update params :btn/mods conj :cta)))

(comment
  (cta {:label "hey"}))


(defn icon [icon & [{:btn/keys [title managed-colors? danger? active? on-click round?] :as opts}]]
  [:button
   {:title (or title (name icon))
    :class (bem/bem-str :button :icon
                       (if round? :round)
                       (if danger? :danger)
                       (if active? :active))
    on-click-prop on-click}
   [svgs/icon icon {:active?        active? :danger? danger?
                    :managed-colors managed-colors?}]])
