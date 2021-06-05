(ns space-ui.inputs.date-time
  "Uses datetime-local input"
  (:require [reagent.dom :as r]
            [reagent.core :as rc]
            [goog.string :as gs]
            [commons.constants.keycodes :as kc]
            [shared-views.svgs :as svgs]
            [commons.logging :as log]
            [commons.dom :as dom]))


(defn- -data-attrs-mapper [[k v]]
  (vector (str "data-" (name k)) v))

(defn render-data-attrs [hmap]
  (into {} (map -data-attrs-mapper hmap)))


(defn format-for-dt-local [^js/Date d]
  (str ""  (.getFullYear d)
       "-" (gs/padNumber (inc (.getMonth d)) 2)
       "-" (gs/padNumber (.getDate d) 2)
       "T" (gs/padNumber (.getHours d) 2)
       ":" (gs/padNumber (.getMinutes d) 2)))

(defn dispatch-on-keycode [dispatch-map]
  (rc/partial dom/dispatch-on-keycode dispatch-map))

(defn datetime-input
  [id
   {:keys
    [placeholder
     on-blur
     on-change
     on-intent
     on-key-down
     process-paste] :as opts}]
  (let [node          (rc/atom nil)
        cur-val       (atom nil)
        get-cur-value
        (fn ^js/Date gcv []
          (if-let [n @node]
            (try
              (js/Date. (.-value n))
              (catch js/Error e
                (log/error e)
                @cur-val))))

        on-key-down
        (dispatch-on-keycode {::kc/enter #(some-> @node (.blur))})

        on-blur-internal
        (if-not on-change
          identity
          (fn [evt]
            (let [parsed-val (get-cur-value)]
              (when (not= parsed-val @cur-val)
                (on-change {:value  parsed-val
                            :target @node})))
            (if on-blur (on-blur))))

        on-paste-internal
        (if-not on-change
          identity
          (fn [evt]
            (let [cur-value (get-cur-value)]
              (when (not= cur-value @cur-val)
                (let [paste-processed
                      (if process-paste
                        (process-paste cur-value)
                        cur-value)]
                  (on-change {:value  paste-processed
                              :target @node}))))))]
    (rc/create-class
      {:display-name "SpaceInputDateTime"

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
                       placeholder css-class
                       ^js/Date value] :as opts}] ;; remember to repeat parameters
         (reset! cur-val value)
         (let [id-str (if (keyword? id) (name id) (str id))
               data-attrs (render-data-attrs data)]
           [:input.space-ui-datetime-input
            (cond->
              {:id           id-str
               :placeholder  placeholder
               :class        (if css-class (name css-class))
               :type         :datetime-local
               :defaultValue (format-for-dt-local value)
               :autoFocus    (:autofocus opts)
               :spellCheck   "false"
               :on-paste     on-paste-internal
               :on-key-down  on-key-down
               :on-focus     on-focus
               :on-blur      on-blur-internal}
              data-attrs (merge data-attrs))]))})))



(defn datepicker
  [{eid :id :keys [due_date] :as task}
   {:keys [on-change] :as opts}]
  (let [state (rc/atom {::expanded? false
                        ::input-id (str "task-" eid "-datetime")})
        collapse!      #(swap! state assoc ::expanded? false)
        toggle-expand! #(swap! state update ::expanded? not)]
    (fn []
      [:div.space-datepicker
       [:label.space-datepicker__label
        {:for (::input-id @state)
         :on-click toggle-expand!}
        [svgs/icon :icons/calendar {:on-click toggle-expand!}]]
       (if (::expanded? @state)
         [datetime-input (::input-id @state)
          {:css-class  "space-datepicker__input g-focusable"
           :on-change  on-change
           :on-blur    collapse!
           :autofocus  true
           :data       {:entity-id eid}
           :value due_date}])])))
