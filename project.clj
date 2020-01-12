(defproject space-ui "20-01--0.0.1"
  :dependencies
  [[org.clojure/clojure    "1.10.1"]
   [org.clojure/spec.alpha "0.2.176"]
   ;
   [page-renderer              "0.4.4"]
   [garden                     "1.3.9"]
   [integrant                  "0.7.0"]
   [aleph                      "0.4.6"]
   ;
   ;
   [compojure                   "1.6.1"]
   [clj-time                    "0.15.1"]
   [hiccup                      "1.0.5"]]

  :min-lein-version "2.5.3"

  :aliases {"dev"   ["do" "clean"]
            "build" ["do" "clean"]}

  :profiles
  {:dev
   {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                   [org.clojure/tools.nrepl     "0.2.13"]
                   [org.clojure/tools.trace     "0.7.10"]
                   [org.slf4j/slf4j-simple      "1.7.25"]]

    :repl-options {:init    (user/run :backend)
                   :init-ns user
                   :timeout 120000}

    :plugins      [[lein-doo "0.1.8"]
                   [lein-pdo "0.1.1"]]}

   :dev-backend
   {:repl-options {:init (user/run :backend), :init-ns user, :timeout 120000}}}

  :main router.server

  :uberjar-name "space-ui.jar")
