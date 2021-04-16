(ns space-ui.media
  (:require [space-ui.image :as image]
            [space-ui.video :as video]
            [space-ui.primitives :as prim]
            [garden.stylesheet :as gs]
            [space-ui.style.constants :as sc]))

(def style-rules
  (list
    [:picture
     {:line-height 0}]

    [:.media.g-active>picture
     :.media.g-active>video
     {:box-shadow "0 0 2px white"}]

    [:.media
     {:display :grid
      :align-items :start}]


    [:.media-gallery
     (prim/grid
       {:grid/gap :8px
        :grid/place-items :center
        :grid/template
        [["main" :auto]
         ["previews" :100px]
         [:auto]]})
     [:&__main
      {:display :grid
       :place-items :center}]

     [:&__previews
      {:display           :grid
       :grid-gap          :8px
       :grid-auto-flow    :column
       :place-self        "stretch"
       :max-height        :100px
       :overflow          :hidden
       :grid-auto-columns "minmax(80px, 140px)"
       :place-items       "stretch center"}
      [:&>.media
       {:cursor :pointer}]
      (gs/at-media sc/mq-phone-and-smaller
        [:&>.media
         {:max-height :50px
          :overflow :hidden}])]]))

(defn single-media
  [{media-name :media/name
    media-type :media/type
    :img/keys [alt srcset sizes css-class]
    :media/keys [alt srcset sizes css-class css-class--container active?]
    :as media}]
  (if (= :media.type/video media-type)
    [:div.media.media--video
     {:class (str css-class--container (if active? " g-active"))}
     (video/face media)]
    [:div.media.media--picture
     {:class (str css-class--container (if active? " g-active"))}
     (image/picture media)]))


(defn gallery [medias]
  [:div.media-gallery
   [:div.media-gallery__main
    (single-media (first medias))]
   [:div.media-gallery__previews
    (when-let [medias (some-> medias not-empty vec
                              (assoc-in [0 :media/active?] true))]
      (map single-media medias))]])

(defn face
  [{media-name :media/name
    :img/keys [alt srcset sizes css-class]
    :media/keys [alt sources sizes css-class]
    :as media}]
  (if (or (vector? media) (seq? media))
    (gallery media)
    (single-media media)))
