(ns space-ui.image
  (:require [clojure.string :as s]
            [clojure.string :as str]))



(defn image
  [{:space-ui/keys [alt src css-class widths]
    :or {widths [400 1200 1600 3000]}}]
  (let [path-components (s/split src #"\/")
        filename (last path-components)
        dirpath (s/join "/" (butlast path-components))
        w-width (fn [w src] (str dirpath "/" "w" w "-" src))
        src-set (map #(str (w-width % filename) " " % "w") widths)]
    [:img
      {:class css-class
       :src    (str dirpath "/" "w" (first widths) "-" src)
       :alt    alt
       :srcset (s/join "," src-set)
       :sizes  (s/join ","
                       ["(min-width: 300px) 400px"
                        "(min-width: 400px) 1200px"
                        "(min-width: 1400px) 1600px"
                        "(min-width: 1600px) 3000px"])}]))


(defn simple
  [{:space-ui/keys [alt srcset sizes css-class]}]
  [:img
   {:class  css-class
    :alt    alt
    :srcset (cond->> srcset (coll? srcset) (str/join ", "))
    :sizes  (cond->> sizes  (coll? sizes) (str/join ", "))}])

(comment
  (simple
    #:space-ui{:alt "Hm"
               :css-class "img"
               :sizes ["(min-width: 300px) 400px"]
               :srcset ["/features/w400.png 400w", "ff"]})
  (simple
    #:space-ui{:alt "Hm"
               :css-class "img"
               :sizes ""
               :srcset "/features/w400.png 400w"}))

(defn picture
  [{:img/keys [alt title lazy? eager? src sources css-class]}]
  ; https://css-tricks.com/a-native-lazy-load-for-the-web-platform/
  (let [loading (cond lazy? "lazy"  eager? "eager"  :else "auto")]
    (if-not sources
      [:picture {:title title :class css-class :loading loading}
       [:source {:srcset src}]
       [:img {:alt (or alt title) :src src :loading loading}]]
      [:picture {:title title :class css-class :loading loading}
       (for [{:img/keys [srcset w type media]} sources]
         [:source {:srcset srcset :type type :media media}])
       [:img {:alt (or alt title) :src src :loading loading}]])))
