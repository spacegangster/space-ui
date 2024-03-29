(ns space-ui.inputs.date-time-2
  "A box with date and time inputs.
   And Save and Cancel buttons"
  (:require ["../util/feature-detect" :refer [isDateSupported isTimeSupported]]
            [garden.core :as garden]
            [reagent.core :as rc]
            [goog.string :as gs]
            [space-ui.inputs.text :as inputs.text]
            [space-ui.style.constants :as sc]
            [space-ui.tiny.btn :as btn]))


(def date-supported? (isDateSupported))
(def time-supported? (isTimeSupported))


(def style-rules
  (list
    btn/style-rules
    [:.space-ui-date-time-2
     {:width :min-content}
     [:&__row
      [:&__input
       {:width    (sc/d-step-x-px 18)
        :position :relative}

       ["&::-webkit-calendar-picker-indicator"
        {:position :absolute
         :left     (sc/d-step-x-px 12)
         :top      0
         :width    :100%
         :height   :100%
         :margin   0
         :padding  0
         :cursor   :pointer}]]]]))


(defn ld-str [^js/Date d]
  (try
    (if-not d
      ""
      (str "" (.getFullYear d)
           "-" (gs/padNumber (inc (.getMonth d)) 2)
           "-" (gs/padNumber (.getDate d) 2)))
    (catch js/Error e
      (def e1 e))))


(defn lt-str [^js/Date d]
  (if-not d
    ""
    (str "" (gs/padNumber (.getHours d) 2)
         ":" (gs/padNumber (.getMinutes d) 2))))

(defn conform-date-val [v]
  v)
(defn conform-time-val [v]
  v)

(def ^:dynamic parse-date-or-nil
  (fn [v timezone]
    (try
      (js/Date. v)
      (catch js/Error e
        (prn ::failed-to-parse-date v)))))


(defn face
  [{^string label   :comp/label
    ^js/Date value  :comp/value
    autofocus-date? :comp/autofocus-date?
    autofocus-time? :comp/autofocus-time?
    on-change-v     :comp/on-change--value
    on-submit       :comp/on-submit
    on-cancel       :comp/on-cancel
    id-base         :comp/id-base}]
  (assert (instance? js/Date value) (str "value must be a date here, got " (type value)))
  (let [[dv tv] [(ld-str value) (lt-str value)]
        atom:date-str (rc/atom dv)
        atom:time-str (rc/atom tv)
        atom:timezone-str (rc/atom "")
        date-id (str id-base "__date")
        time-id (str id-base "__time")

        -form-date-or-nil
        (fn []
          (let [vd @atom:date-str
                vt (or (not-empty @atom:time-str) "00:00")]
            (when (not-empty vd)
              (let [v (str vd "T" vt)]
                (parse-date-or-nil v @atom:timezone-str)))))

        -on-submit #(on-submit (-form-date-or-nil))

        -on-change
        (fn []
          (when-let [new-date (-form-date-or-nil)]
            (prn ::date-time new-date)
            (if on-change-v
              (on-change-v new-date))))

        on-input__date--text
        (fn [v]
          (reset! atom:date-str (conform-date-val v))
          (-on-change))

        on-input__date
        (fn [v]
          (reset! atom:date-str v)
          (-on-change))

        -on-time-commit -on-submit
        -on-date-commit -on-submit

        on-input__time--text
        (fn [v]
          (reset! atom:time-str (conform-time-val v))
          (-on-change))

        on-input__time
        (fn [v]
          (reset! atom:time-str v)
          (-on-change))]

    (fn []
      [:div.space-ui-date-time-2.g-grid-rows.g-gap2

       ; Date
       [:div.space-ui-date-time-2__row.g-grid-rows.g-gap1
        (if label
          [:label.g-margin-bottom-1 [:strong label]])
        [:div.g-grid-cols-compact.g-gap2
         [:label.space-ui-date-time-2__row__label
          {:for date-id} "date"]

         [inputs.text/face
          {:comp/id               date-id
           :comp/css-class        "space-ui-date-time-2__row__input"
           :comp/input-type       (if date-supported? :input.type/date :input.type/text)
           :comp/appearance       :appearance/transparent
           :comp/value            dv
           :comp/autofocus?       autofocus-date?
           :comp/placeholder      (if-not date-supported? "YYYY-MM-DD")
           :comp/intents          {:intents/commit -on-date-commit}
           :comp/on-change--value (if time-supported?
                                    on-input__date
                                    on-input__date--text)}]]

        (if-not date-supported?
          [:div.g-hint "Date like 2020-06-14"])]


       ; Time
       [:div.space-ui-date-time-2__row.g-grid-rows.g-gap1
        [:div.g-grid-cols-compact.g-gap2
         [:label.space-ui-date-time-2__row__label
          {:for time-id} "time"]

         [inputs.text/face
          {:comp/id               time-id
           :comp/css-class        "space-ui-date-time-2__row__input"
           :comp/input-type       (if time-supported? :input.type/time :input.type/text)
           :comp/appearance       :appearance/transparent
           :comp/value            tv
           :comp/autofocus?       autofocus-time?
           :comp/placeholder      (if-not time-supported? "HH:mm")
           :comp/intents          {:intents/commit -on-time-commit}
           :comp/on-change--value (if time-supported?
                                    on-input__time
                                    on-input__time--text)}]]

        (if-not time-supported?
          [:div.g-hint "24hr time like 16:20 or 03:01"])

        (if on-submit
          [:div.g-gap2.g-margin-top-1.g-grid-cols.g-grid-cols--space-between
           [btn/cta-blue
            {:btn/label    "Save"
             :btn/on-click -on-submit}]
           [btn/btn
            {:btn/label "Cancel"
             :btn/on-click on-cancel}]])]])))




(defn ^:dev/card? dev-card1 [ctx]
  (let [atom:value (rc/atom #inst "2021-01-01T22:00:00+03:00")
        atom:value2 (rc/atom nil)]
    {:dev/card?         true
     :dev.card/label    "Date and Time"
     :dev.card/gradient  "linear-gradient(to right, hsl(338, 59%, 40%), hsl(6, 58%, 37%))"
     :dev.card/category :dev.card.category/inputs
     :dev.card/style-fn (fn [] [:style (garden/css style-rules)])
     :dev.card/face
                        (fn []
                          [:div.g-grid-rows.g-gap3
                           [:div.g-pane.g-pad-2.g-width-min
                            [face {:comp/on-change--value #(reset! atom:value %)
                                   :comp/id-base          "task-3-date-time"
                                   :comp/value            @atom:value}]
                            [:br] [:br]
                            [:span "result: " (str @atom:value)]]

                           [:div.g-pane.g-pad-2.g-width-min
                            [face {:comp/on-change--value #(reset! atom:value2 %)
                                   :comp/id-base          "task-4-date-time"
                                   :comp/value            @atom:value2}]
                            [:br] [:br]
                            [:span "result: " (str @atom:value2)]]])}))
