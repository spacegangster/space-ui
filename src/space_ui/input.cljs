(ns space-ui.input
  (:require [reagent.core :as r]
            [space-ui.style.constants :as sc]
            [space-ui.ui-logic.user-intents :as user-intents]))


(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))


(def styles-src
  (list
    {:border         :none}
    {:border-bottom  "1px solid white"}
    {:padding        "6px 0px"
     :border-radius  :2px
     :width          :100%
     :letter-spacing :.09em
     :outline        :none
     :font-size      :inherit
     :font-family    :inherit
     :display        :inline-block}
    ["&:empty"
     {:min-width :220px}]
    ["&:empty::before"
     {:content "attr(placeholder)"}]))

(def styles
  [:.space-ui-input styles-src])

(defn root
  [id
   {:keys
    [^js/String placeholder
     ^IMap intents
     ^js/Function on-change
     ^js/Function parse-fn
     ^js/Function on-change-complete ; todo
     ^js/Function on-intent
     ^js/Function on-key-down
     ^js/Function process-paste]
    :as opts}]
  (let [node          (r/atom nil)
        cur-val       (atom nil)
        parse-fn      (or parse-fn identity)
        get-cur-value #(some-> @node (.-value) parse-fn)

        on-key-down (cond
                      intents (r/partial user-intents/handle-intent-with-intents-map intents)
                      on-intent #(some-> % user-intents/key-down-evt->intent-evt on-intent)
                      on-key-down on-key-down
                      :else identity)

        on-blur-internal
        (fn [evt]
            (if on-change-complete
              (on-change-complete {:value (get-cur-value)})))

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
       (fn [id {input-name :name
                :keys [data ; @param {map} with data attributes
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
               :tabIndex     1
               :defaultValue value
               :autoFocus    (:autofocus opts)
               :spellCheck   "false"
               :name         input-name
               :on-key-up    on-key-up-internal
               :on-paste     on-paste-internal
               :on-key-down  on-key-down
               :on-focus     on-focus
               :on-blur      on-blur}
              data-attrs (merge data-attrs))]))})))

