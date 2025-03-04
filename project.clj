(defproject ell-iot "0.1.0-SNAPSHOT"

  :description "Microservice aimed to provide a way to enable Arduino like devices to produce metrics to Prometheus and Grafana."

  :url "https://github.com/macielti/ell-iot"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.0"]

                 [net.clojars.macielti/common-clj "43.74.74"]
                 [net.clojars.macielti/prometheus-component "1.4.1-1"]
                 [net.clojars.macielti/service-component "2.4.2"]

                 [com.taoensso/timbre "6.6.1"]

                 [com.github.clj-easy/graal-build-time "1.0.5"]]

  :profiles {:dev {:plugins        [[lein-shell "0.5.0"]
                                    [com.github.liquidz/antq "RELEASE"]
                                    [com.github.clojure-lsp/lein-clojure-lsp "1.4.17"]]

                   :resource-paths ["resources"]

                   :test-paths     ["test/unit" "test/integration" "test/helpers"]

                   :dependencies   [[com.github.igrishaev/pg2-migration "0.1.33"]
                                    [hashp "0.2.2"]]

                   :injections     [(require 'hashp.core)]

                   :aliases        {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
                                    "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
                                    "diagnostics"  ["clojure-lsp" "diagnostics"] ;; check if project has any diagnostics (clj-kondo findings)
                                    "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]] ;; check all above
                                    "clean-ns-fix" ["clojure-lsp" "clean-ns"] ;; Fix namespaces not clean
                                    "format-fix"   ["clojure-lsp" "format"] ;; Fix namespaces not formatted
                                    "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]] ;; Fix both

                                    "native"       ["shell"
                                                    "native-image"
                                                    "--no-fallback"
                                                    "--enable-url-protocols=http,https"
                                                    "-march=compatibility"
                                                    "--report-unsupported-elements-at-runtime"

                                                    "--initialize-at-build-time"

                                                    ;;prometheus
                                                    "--initialize-at-run-time=io.prometheus.client.Striped64"

                                                    "-H:ReflectionConfigurationFiles=reflect-config.json"
                                                    "--features=clj_easy.graal_build_time.InitClojureClasses"
                                                    "-Dio.pedestal.log.defaultMetricsRecorder=nil"
                                                    "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
                                                    "-H:+UnlockExperimentalVMOptions"
                                                    "-H:+StaticExecutableWithDynamicLibC"
                                                    "-H:Name=./target/${:name}"]}}}
  :main ell-iot.components)
