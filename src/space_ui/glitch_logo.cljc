(ns space-ui.glitch-logo
  (:require [garden.core :as garden]
            [garden.stylesheet :as gs]))

(def ff-set-logo
  (str "-apple-system, system-ui, BlinkMacSystemFont,"
       " 'Josefin Sans', 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif"))

(def style
  [:style
   (garden/css

     (gs/at-keyframes :flicker-before
       [:0%  {:left "-2px"}]
       [:10% {:left "0px"}]
       [:20% {:left "-2px"}])
     (gs/at-keyframes :flicker-after
       [:0%  {:left "2px"}]
       [:10% {:left "0px"}]
       [:20% {:left "2px"}])

     [".glitch-logo-word::before"
      {:animation "3s steps(1) flicker-before infinite"}]

     [".glitch-logo-word::after"
      {:animation "3s steps(1) flicker-after infinite"}]

     [:.glitch-logo
      {:font-family     ff-set-logo
       :font-weight     100
       :font-size       "6rem"
       :display         "flex"
       :flex-wrap       "wrap"
       :line-height     "1"
       :letter-spacing  ".07em"
       :margin-bottom   ".4em"
       :position        "relative"
       :left            "-0.03em"
       :text-align      "justify"}

      [:&-word
       {:margin-left    ".56em"
        :margin-bottom  ".50em"
        :position       "relative"
        :z-index        "2"
        :font-style     "italic"}
       [:&-text
         {:position "relative"
          :left "4px"
          :color "hsla(120, 90%, 70%, .4)"}]
       ["&::after"
        "&::before"
         {:content   "attr(data-text)"
          :position  "absolute"
          :z-index   "1"
          :left      "2px"
          :top       "2px"
          :color     "hsla(0, 90%, 70%, .9)"}]
       ["&::before"
        {:left   "-2px"
         :color  "hsla(200, 90%, 70%, .9)"}]
       ["&:first-of-type"
        {:margin-left "0em"}]]]

     (gs/at-media {:max-width "429px"}
       [:.glitch-logo
        {:font-size "4rem"}]))])


(defn logo-glitched [& words]
  [:h1.glitch-logo
   style
   (for [w words]
    [:div.glitch-logo-word {:data-text w}
     [:span.glitch-logo-word-text w]])])
