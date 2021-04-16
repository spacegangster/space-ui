(ns space-ui.inputs.timezone-select
  (:require [space-ui.const.timezones :as const.tz]))

(defn- -zone-option
  [{:option/keys [value label]}]
  [:option {:value value} label])

(def style-rules
  [:.space-ui-select
   {:font-size      :1em
    :font-weight    :inherit
    :letter-spacing :inherit
   ;:background     :none
   ;:border         :none
    :min-height     :1em
    :color          :inherit}
   {:padding :4px
    :max-width :100%}])

(defn face
  [{field-name :comp/name
    :comp/keys
    [id default-value
     on-change]}]
  (let [on-change-internal
        #?(:cljs (fn [react-evt]
                   (let [t (^js .-target react-evt)
                         v (^js .-value t)
                         e #:evt{:type   :event-types/change
                                 :value  (not-empty v)
                                 :target t}]
                     (prn ::e e)
                     (if on-change
                       (on-change e))))
           :clj nil)]
    [:select.space-ui-select
     {:id id :name field-name
      :on-change #?(:cljs on-change-internal :clj nil)
      :default-value  default-value}
     (for [tz const.tz/list1]
       ^{:key (:option/value tz)}
       [-zone-option tz])]))

(comment
  (face #:comp{:name "zone" :id "son"}))