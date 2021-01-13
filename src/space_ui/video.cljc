(ns space-ui.video)


(defn face
  [{:media/keys [alt title lazy? eager? loop? srcset src sources sizes css-class]}]
  ; https://css-tricks.com/a-native-lazy-load-for-the-web-platform/
  (let [loading (cond lazy? "lazy"  eager? "eager"  :else "auto")]
    (cond
      src
      [:video {:title title :class css-class
               :loop loop?
               :playsinline true
               :autoplay true
               :src src
               :loading loading}]

      sources
      [:video {:title title :class css-class
               :loop loop?
               :playsinline true
               :autoplay true
               :muted true
               :loading loading}
              ;:controls true}
        (for [{:media/keys [src src-type media] :as source} sources]
          [:source {:type src-type :src src :media media}])])))

