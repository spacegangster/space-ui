(ns space-ui.image
  (:require [clojure.string :as s]))



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
