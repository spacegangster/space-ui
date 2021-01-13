(ns space-ui.photo-gallery
  (:require [common.image :as image]
            [clojure.java.io :as io]
            [cheshire.core :as cheshire]
            [space-ui.image :as space-image]))

(defn prefix [s]
  (str "/website/features/" s))

(defn w-width [w src]
  (prefix (str "w" w "-" src)))

(defn pswp-data [src]
  (let [preview-src (w-width 400 src)
        main-src (w-width 3000 src)
        rp (str "public" main-src)
        [w h] (image/dimensions<-res-path rp)]
    {:msrc preview-src
     :src main-src
     :w w
     :h h}))

(defn pswp-data2
  [{:img/keys [src src-preview title width height]}]
  {:msrc  src-preview
   :src   src
   :title title
   :w     width
   :h     height})

(defn pswp-data-w-alt [[alt src]]
  (assoc (pswp-data src) :title alt))

(defn photo-gallery [gal-id srcs]
  [:div.gal {:id gal-id :data-pswp (cheshire/generate-string (map pswp-data srcs))}
   (for [src srcs]
     (space-image/image
       #:space-ui{:src (str "/website/features/" src)
                  :css-class "gal__img"}))])

(defn photo-gallery-w-alts [gal-id srcs]
  [:div.gal {:id gal-id :data-pswp (cheshire/generate-string (map pswp-data-w-alt srcs))}
   (for [[alt src] srcs]
     (space-image/image
       #:space-ui{:src (str "/website/features/" src)
                  :alt alt
                  :css-class "gal__img"}))])


(defn face [{:space-ui/keys [id images]}]
  [:div.gal {:id id :data-pswp (cheshire/generate-string (map pswp-data2 images))}
   (for [{:img/keys [src srcset title sizes srcset--webp]} images]
     (space-image/picture
       #:space-ui{:sizes     sizes
                  :srcset    srcset
                  :src src
                  :srcset--webp srcset--webp
                  :alt       title
                  :css-class "gal__img"}))])

