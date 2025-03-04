(ns ell-iot.components
  (:require [integrant.core :as ig]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.tools.logging]
            [common-clj.integrant-components.config :as component.config]
            [common-clj.integrant-components.routes :as component.routes]
            [ell-iot.diplomat.http-server :as diplomat.http-server]
            [prometheus-component.core :as component.prometheus])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def dependencies
  {:config     (ig/ref ::component.config/config)
   :prometheus (ig/ref ::component.prometheus/prometheus)})

(def components
  {::component.config/config         {:path "resources/config.edn"
                                      :env  :prod}
   ::component.prometheus/prometheus {:metrics []}
   ::component.routes/routes         {:routes diplomat.http-server/routes}
   ::component.service/service       {:components (merge dependencies
                                                         {:routes (ig/ref ::component.routes/routes)})}})

(defn start-system! []
  (timbre/set-min-level! :debug)
  (ig/init components))

(def -main start-system!)
