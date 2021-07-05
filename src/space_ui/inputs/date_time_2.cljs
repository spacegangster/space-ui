(ns space-ui.inputs.date-time-2
  "A box with date and time inputs.
   And Save and Cancel buttons"
  (:require [common.const.gradients :as gradients]
            ["../util/feature-detect" :refer [isDateSupported isTimeSupported]]
            [garden.core :as garden]
            [reagent.core :as rc]
            [goog.string :as gs]
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
  [{^string label  :comp/label
    ^js/Date value :comp/value
    on-change-v    :comp/on-change--value
    on-submit      :comp/on-submit
    on-cancel      :comp/on-cancel
    id-base        :comp/id-base}]
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
        (fn [^js r-evt]
          (let [v (-> r-evt (.-target) (.-value) conform-date-val)]
            (reset! atom:date-str v)
            (-on-change)))

        on-input__date
        (fn [^js r-evt]
          (let [v (-> r-evt (.-target) (.-value))]
            (reset! atom:date-str v)
            (-on-change)
            (prn ::date (aget r-evt "target" "value"))))

        on-input__time--text
        (fn [^js r-evt]
          (let [v (-> r-evt (.-target) (.-value) conform-time-val)]
            (reset! atom:time-str v)
            (-on-change)))

        on-input__time
        (fn [^js r-evt]
          (let [v (-> r-evt (.-target) (.-value))]
            (reset! atom:time-str v)
            (-on-change)
            (prn ::time (aget r-evt "target" "value"))))]

    (fn []
      [:div.space-ui-date-time-2.g-grid-rows.g-gap2
       [:div.space-ui-date-time-2__row.g-grid-rows.g-gap1
        (if label
          [:label.g-margin-bottom-1 [:strong label]])
        [:div.g-grid-cols-compact.g-gap2
         [:label.space-ui-date-time-2__row__label
          {:for date-id} "date"]

         (if date-supported?
           [:input.space-ui-date-time-2__row__input
            {:id           date-id
             :type         "date"
             :defaultValue dv
             :on-input     on-input__date}]
           [:input.space-ui-date-time-2__row__input
            {:id           date-id
             :type         "text"
             :placeholder  "YYYY-MM-DD"
             :defaultValue dv
             :on-input     on-input__date--text}])]

        (if-not date-supported?
          [:div.g-hint "Date like 2020-06-14"])]


       [:div.space-ui-date-time-2__row.g-grid-rows.g-gap1
        [:div.g-grid-cols-compact.g-gap2
         [:label.space-ui-date-time-2__row__label
          {:for time-id} "time"]
         (if time-supported?
           [:input.space-ui-date-time-2__row__input
            {:id           time-id
             :type         "time"
             :defaultValue tv
             :on-input     on-input__time}]

          [:input.space-ui-date-time-2__row__input
           {:id           time-id
            :type         "time"
            :placeholder  "HH:mm"
            :defaultValue tv
            :on-input     on-input__time--text}])]

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
     :dev.card/gradient (gradients/id->expression :ui.gradient/alive-darker)
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
