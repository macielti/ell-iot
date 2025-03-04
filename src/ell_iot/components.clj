(ns ell-iot.components
  (:require [common-clj.integrant-components.config :as component.config]
            [common-clj.integrant-components.routes :as component.routes]
            [ell-iot.diplomat.http-server :as diplomat.http-server]
            [integrant.core :as ig]
            [prometheus-component.core :as component.prometheus]
            [service-component.core :as component.service]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.tools.logging])
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
