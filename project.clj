(def extern-libs [])

(defproject centrebull "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[bouncer "1.0.0"]
                 [buddy "1.3.0"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [cljs-ajax "0.5.8"]
                 [compojure "1.5.2"]
                 [bk/ring-gzip "0.3.0"]
                 [metosin/compojure-api "1.1.10"]
                 [conman "0.6.3"]
                 [cprop "0.1.10"]
                 [clj-time "0.13.0"]
                 [luminus-immutant "0.2.3"]
                 [luminus-migrations "0.2.9"]
                 [luminus-nrepl "0.1.4"]
                 [markdown-clj "0.9.94"]
                 [metosin/ring-http-response "0.8.1"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.473" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.postgresql/postgresql "9.4.1212"]
                 [camel-snake-kebab "0.4.0"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [re-frame "0.9.1"]
                 [com.rpl/specter "1.0.1"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [reagent "0.6.0"]
                 [reagent-utils "0.2.0"]
                 [ring-middleware-format "0.7.2"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-defaults "0.2.3"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [selmer "1.10.6"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main centrebull.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.1"]
            [migratus-lein "0.4.3"]
            [lein-cljsbuild "1.1.4"]
            [lein-immutant "2.1.0"]
            [lein-kibit "0.1.2"]]
  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :figwheel
  {:http-server-root "public"
   :nrepl-port       7002
   :css-dirs         ["resources/public/css"]
   :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}


  :profiles
  {:uberjar       {:omit-source    true
                   :prep-tasks     ["compile" ["cljsbuild" "once" "min"]]
                   :cljsbuild
                                   {:builds
                                    {:min
                                     {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                                      :compiler
                                                    {:output-to        "target/cljsbuild/public/js/app.js"
                                                     :optimizations    :advanced
                                                     :pretty-print     false
                                                     :closure-warnings {:non-standard-jsdoc :off}
                                                     :foreign-libs     ~extern-libs
                                                     :externs          ["react/externs/react.js" "src/externs.js"]}}}}


                   :aot            :all
                   :uberjar-name   "centrebull.jar"
                   :source-paths   ["env/prod/clj"]
                   :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev   {:dependencies   [[prone "1.1.4"]
                                    [ring/ring-mock "0.3.0"]
                                    [ring/ring-devel "1.5.1"]
                                    [pjstadig/humane-test-output "0.8.1"]
                                    [binaryage/devtools "0.9.0"]
                                    [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                    [doo "0.1.7"]
                                    [faker "0.2.2"]
                                    [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                                    [org.clojure/data.generators "0.1.2"]
                                    [figwheel-sidecar "0.5.9"]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.18.1"]
                                    [lein-doo "0.1.7"]
                                    [lein-figwheel "0.5.9"]
                                    [org.clojure/clojurescript "1.9.473"]]
                   :cljsbuild
                                   {:builds
                                    {:app
                                     {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                                      :compiler
                                                    {:main          "centrebull.app"
                                                     :asset-path    "/js/out"
                                                     :output-to     "target/cljsbuild/public/js/app.js"
                                                     :output-dir    "target/cljsbuild/public/js/out"
                                                     :source-map    true
                                                     :optimizations :none
                                                     :foreign-libs  ~extern-libs
                                                     :externs       ["src/externs/externs.js"]
                                                     :pretty-print  true}}}}



                   :doo            {:build "test"}
                   :source-paths   ["env/dev/clj" "test/clj" "test/cljc"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user}
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
   :project/test  {:resource-paths ["env/test/resources"]
                   :cljsbuild
                                   {:builds
                                    {:test
                                     {:source-paths ["src/cljc" "src/cljs" "test/cljc" "test/cljs"]
                                      :compiler
                                                    {:output-to     "target/test.js"
                                                     :main          "centrebull.doo-runner"
                                                     :optimizations :whitespace
                                                     :pretty-print  true}}}}}


   :profiles/dev  {}
   :profiles/test {}})
