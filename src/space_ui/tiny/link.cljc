(ns space-ui.tiny.link
  (:require [space-ui.bem :as bem]
            [space-ui.style.constants :as sc]
            [space-ui.svgs :as svgs]
            [space-ui.primitives :as prim]
            [garden.stylesheet :as gs]))


;; attributes statement for autocompletion
:link/title :link/on-click :link/class :link/href :link/label :link/attrs :link/goal-id
:link/blank? :link/target-blank? :link/outer? :link/colorscheme? :link/underline?
:link/main? :link/luminous? :link/active?


(def on-click-prop #?(:cljs :on-click :clj :onclick))
(def top-right-arrow "⇗")

(def color-text--off (prim/hsl 0 0 93))
(def color-text--stage-1 (prim/hsl 0 0 99))
(def color-text--stage-2 (prim/hsl 0 0 95))

(def color-glow--off     (prim/hsl 0 0 93))
(def color-glow--off:a   (prim/hsla 0 0 93 0.7))
(def color-glow--stage-1 (prim/hsl 0 0 99))
(def color-glow--stage-2 (prim/hsl 0 0 95))

(def btn-state--off
  {:color color-text--off
   :border-color color-glow--off})

(def btn-state--hover
  {:color        color-text--stage-2
   :border-color color-glow--stage-2})

(def btn-state--pressed
  {:color        color-text--stage-2
   :border-color color-glow--stage-2
   :background   (prim/hsla 0 0 0 0.07)})

(def btn-state--focus
 {:color color-text--stage-1
  :border-color color-glow--stage-1
  :box-shadow (str "0 0 2px " color-glow--stage-1)})

(def btn-state--inviting
  {:color color-text--stage-2
   :border-color color-glow--stage-2
   :box-shadow (str "0 0 1px " color-glow--stage-2)})

(def btn-state--supercta
  {:animation "0.7s ease-in btn--supercta infinite alternate"})




(def style-rules
  (list
    (gs/at-keyframes :btn--supercta
      [:0% btn-state--off]
      [:100% btn-state--inviting])

    [:.link
     btn-state--off
     {:border :none}
     [:&:hover
      :&:focus
      btn-state--focus]
     [:&:active
      btn-state--pressed]
     [:&--box
      {:padding (sc/d-step-x-px 1 1.5)
       :letter-spacing :0.04em
       :display :inline-block}]
     [:&--cta
      btn-state--supercta]
     [:&--bordered
      {:display :inline-block
       :border "1px solid white"}]]))


(defn inline
  "Plain link"
  [{:link/keys [title on-click class href label attrs goal-id
                blank? target-blank? outer? colorscheme? underline?
                main? luminous? active?] :as params}]
  (let [css-class
        (-> :link
            (bem/bem-str {:main main? :active active? :luminous luminous?})
            (str (if active? " g-active")
                 (if (not= false underline?) " g-underline")
                 (if colorscheme? " g-colorscheme-control-text")
                 (if class (str " " (name class)))))]
    [:a.g-nolink
     (cond-> {:href         href
              :title        title
              :data-goal-id goal-id
              on-click-prop on-click
              :class        css-class}
             attrs (->> (merge attrs))
             outer? (assoc :rel "noopener")
             (or outer? target-blank? blank?) (assoc :target "_blank"))
     label]))


(defn plain
  "Plain link"
  [{:link/keys [title on-click class href label attrs goal-id
                blank? target-blank? outer? colorscheme? underline?
                main? luminous? active? round? bordered?] :as params}]
  (let [css-class
        (-> :link
            (bem/bem-str {:box true
                          :main main? :round round? :active active?
                          :luminous luminous? :bordered bordered?})
            (str (if active? " g-active")
                 (if underline? " g-underline")
                 (if colorscheme? " g-colorscheme-control-text")
                 (if class (str " " (name class)))))]
    [:a.g-nolink
     (cond-> {:href         href
              :title        title
              :data-goal-id goal-id
              on-click-prop on-click
              :class        css-class}
             attrs (->> (merge attrs))
             outer? (assoc :rel "noopener")
             (or outer? target-blank? blank?) (assoc :target "_blank"))
     label]))


(defn box
  "bordered link"
  [{:link/keys [title on-click class css-class href label attrs goal-id
                blank? target-blank? outer? colorscheme? underline?
                main? luminous? active? round?
                icon icon-after icon-alt] :as params}]
  (let [css-class
        (-> :link
            (bem/bem-str {:box true :bordered true
                          :main main? :round round? :active active?
                          :luminous luminous?})
            (str (if active? " g-active")
                 (if underline? " g-underline")
                 (if colorscheme? " g-colorscheme-control-text g-colorscheme-control-border")
                 (if class (str " " (name class)))
                 (if css-class (str " " (name css-class)))))]
    [:a.g-nolink
     (cond-> {:href         href
              :title        title
              :data-goal-id goal-id
              on-click-prop on-click
              :class        css-class}
             attrs (->> (merge attrs))
             outer? (assoc :rel "noopener")
             (or outer? target-blank? blank?) (assoc :target "_blank"))
     (if icon
       [:div.link__icon
        {:class (if colorscheme? :g-colorscheme-control-icon)
         :title icon-alt}
        icon])
     (if label
       [:div.link__label {:class (if icon "link__label--ml")} label])
     (if icon-after
       [:div.link__icon.link__icon--after
        {:class (if colorscheme? :g-colorscheme-control-icon)
         :title icon-alt}
        icon-after])]))


(defn w-icon
  "Box-like link"
  [{:link/keys [icon icon-alt icon-after
                title on-click class href label attrs goal-id
                blank? target-blank? outer? colorscheme? underline?
                main? luminous? active? round? bordered?] :as params}]
  (let [css-class
        (-> :link
            (bem/bem-str {:box true
                          :main main? :round round? :active active?
                          :luminous luminous? :bordered bordered?})
            (str (if active? " g-active")
                 (if underline? " g-underline")
                 (if colorscheme? " g-colorscheme-control-text")
                 (if class (str " " (name class)))))]
    [:a.g-nolink
     (cond-> {:href         href
              :title        title
              on-click-prop on-click
              :class        css-class}
             attrs (->> (merge attrs))
             outer? (assoc :rel "noopener")
             (or outer? target-blank? blank?) (assoc :target "_blank"))
     (if icon
       [:div.link__icon
        {:class (if colorscheme? :g-colorscheme-control-icon)
         :title icon-alt}
        icon])
     (if label
       [:div.link__label {:class (if icon "link__label--ml")} label])
     (if icon-after
       [:div.link__icon.link__icon--after
        {:class (if colorscheme? :g-colorscheme-control-icon)
         :title icon-alt}
        icon-after])]))

