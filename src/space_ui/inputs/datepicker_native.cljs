(ns space-ui.inputs.datepicker-native
  "Bindigns to native datetime picker, yet to polish and test"
  (:require [garden.core :as garden]
            [goog.string :as g.str]
            [garden.stylesheet :as gs]
            [reagent.core :as rc]
            [space-ui.const.keycodes :as kc]
            [space-ui.bem :as bem]
            [commons.dom :as dom]))

(defn format-for-dt-local [^js/Date d]
  (str ""  (.getFullYear d)
       "-" (g.str/padNumber (inc (.getMonth d)) 2)
       "-" (g.str/padNumber (.getDate d) 2)
       "T" (g.str/padNumber (.getHours d) 2)
       ":" (g.str/padNumber (.getMinutes d) 2)))


(defn format--rec-edit-base [^js/Date d]
  (str ""  (.getFullYear d)
       "-" (g.str/padNumber (inc (.getMonth d)) 2)
       "-" (g.str/padNumber (.getDate d) 2)
       " " (g.str/padNumber (.getHours d) 2)
       ":" (g.str/padNumber (.getMinutes d) 2)))


(defn ua-has? [s]
  (> -1 (.indexOf js/navigator.userAgent s)))

(def config:supports-input-datetime?
  (and (not (ua-has? "Safari"))
       (not (ua-has? "Firefox"))))

(defn- on-time-change--native [on-change-external evt]
  (try
    (let [v (aget evt "target" "value")
          ts (js/Date.parse v)]
      (if (js/isNaN ts)
        (on-change-external nil)
        (on-change-external (js/Date. ts))))
    (catch js/Error err
      (on-change-external nil))))


(def style
  [:style
   (garden/css
     [:.native-date-time-picker
      {:display :flex
       :font-size :14px
       :align-items :center}
      [:&__label
       {:width :136px
        :display :block
        :letter-spacing :.04em}]
      [:&__input
       {:padding "4px 0"
        :width :auto}]]
     (gs/at-media {:min-width :1000px}
       [:.native-date-time-picker--column
        {:flex-direction :column
         :align-items :flex-start}
        ["> .native-date-time-picker__label"
         {:font-size :16px
          :width :auto
          :letter-spacing :0.09em
          :color :gray}]
        ["> .native-date-time-picker__input"
         {:line-height :32px}]]))])



(defn face
  [{:comp/keys
    [label
     ui/layout
     ^js/Date value
     on-change--value]
    :as prms}]
  (let [state (rc/atom {:value value})
        on-commit-internal  (rc/partial on-time-change--native on-change--value)]
    (fn []
      [:div.space-ui-input (bem/bem :native-date-time-picker layout)
       (if label
         [:label.native-date-time-picker__label label])
       [:input.native-date-time-picker__input
        (let [v (:value @state)]
          {:type         "datetime-local"
           :placeholder  (if-not config:supports-input-datetime? "dd/mm/yyyy, --:--")
           :defaultValue (if v (format-for-dt-local v))
           :on-key-down (rc/partial dom/dispatch-on-keycode
                          {::kc/enter on-commit-internal})
           :on-blur      on-commit-internal})]])))
