(ns space-ui.tiny.icon-status
  "Checkmark icon in CSS")

(def style-rules
  [:.icon-status
   {:cursor   :pointer
    :position :relative
    :width    :100%
    :height   :100%
    :color    "hsl(0, 0%, 70%)"}
   [:&__dash1
    :&__dash2
    {:position   :absolute
     :width      :36%
     :border-top "2px solid"
     :top        :56%
     :left       :10%
     :transform  "rotateZ(40deg)"}]
   [:&__dash2
    {:width       :60%
     :top         :52%
     :left        :33.5%
     :transform   "rotateZ(-40deg)"}]
   [:&--complete
    {:color "hsl(0, 0%, 40%)"}
    [:>.icon-status__dash1
     :>.icon-status__dash2
     {:border-top "2px solid"}]]])

(defn- face [status]
  [:div.icon-status (if status {:class "icon-status--complete"})
   [:div.icon-status__dash1]
   [:div.icon-status__dash2]])
