(ns space-ui.input
  (:require [reagent.core :as r]
            [space-ui.ui-logic.user-intents :as user-intents]))


(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))


(defn root [id {:keys [placeholder on-change on-intent on-key-down process-paste] :as opts}]
  (let [node          (r/atom nil)
        cur-val       (atom nil)
        get-cur-value #(some-> @node (.-value))
        on-key-down   (cond
                        on-intent   #(some-> % user-intents/key-down-evt->intent-evt on-intent)
                        on-key-down on-key-down
                        :else       identity)

        on-key-up-internal
        (if on-change
          (fn [evt]
            (let [cur-html (get-cur-value)]
              (when (not= cur-html @cur-val)
                (on-change {:value cur-html
                            :target @node})))))

        on-paste-internal
        (if on-change
          (fn [evt]
            (let [cur-html (get-cur-value)]
              (when (not= cur-html @cur-val)
                (let [paste-processed (if process-paste
                                        (process-paste cur-html)
                                        cur-html)]
                  (on-change {:value paste-processed
                              :target @node}))))))]
    (r/create-class
      {:display-name "SpaceInput"

       :component-did-mount
       (fn [this]
         (reset! node (r/dom-node this)))

       :should-component-update
       (fn [this cur-argv [f id next-props :as next-argv]]
         (and (not= @node js/document.activeElement)
              (not= (get-cur-value) (:value next-props))))

       :reagent-render
       (fn [id {:keys [data ; @param {map} with data attributes
                       on-blur on-focus
                       placeholder css-class value] :as opts}] ;; remember to repeat parameters
         (reset! cur-val value)
         (let [id-str (if (keyword? id) (name id) (str id))
               data-attrs (render-data-attrs data)]
           [:input.space-ui-input
            (cond->
              {:id           id-str
               :placeholder  placeholder
               :class        (if css-class (name css-class))
               :defaultValue value
               :autoFocus    (:autofocus opts)
               :spellCheck   "false"
               :on-key-up    on-key-up-internal
               :on-paste     on-paste-internal
               :on-key-down  on-key-down
               :on-focus     on-focus
               :on-blur      on-blur}
              data-attrs (merge data-attrs))]))})))

