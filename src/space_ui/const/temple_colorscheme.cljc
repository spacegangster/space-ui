(ns space-ui.const.temple-colorscheme)

(defn- hsl [h s l]
  (str "hsl(" h ", " s "%," l"%)"))

; ### Tissue compatibility analytics
; black background
(def c-bg (hsl 33 43 8))

; neutral tiles
(def c-ind-inactive   (hsl  20 21 16)) ; inactive neutral
(def c-ind-inactive2  (hsl  30 21 16)) ; inactive neutral
(def c-ind-activating (hsl 178 23 29)) ; activating neutral

; blueish indicators
(def c-ind-inactive3 (hsl 189 14 46)) ; inactive part
(def c-ind-active    (hsl 179 36 68)) ; active part

; green fonts
(def c-green-font-active (hsl 150 95 75)) ; active part
(def c-green-font-active (hsl 150 89 78)) ; active part
(def c-green-font-active (hsl 144 89 79)) ; active part

; errors
(def c-ind-error-bg-active      (hsl 359, 88, 74)) ; active error
(def c-ind-error-bg-semi-active (hsl 356, 59, 46)) ; semi active error
(def c-ind-error-bg-inactive (hsl   4, 73, 22)) ; inactive error
(def c-ind-desync            c-ind-inactive2) ; inactive error
(def c-ind-bg--main-cell (hsl 10 65 54))
(def c-ind-fg--main-cell (hsl 10 65 8))


; ### Direct Tissue scanner
(def c-tissue-scanner-bg  (hsl  42 21 73)) ; white background
(def c-tissue-scanner-fg  (hsl   6 34 55)) ; red on white text
(def c-tissue-scanner-fg2 (hsl 356 77 29)) ; red on skin text

