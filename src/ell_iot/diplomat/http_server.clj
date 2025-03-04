(ns ell-iot.diplomat.http-server
  (:require [prometheus-component.core :as component.prometheus]
            [service-component.interceptors :as service.interceptors]
            [ell-iot.diplomat.http-server.metric :as http-server.metric]))

(def routes [["/metrics" :post [service.interceptors/http-request-in-handle-timing-interceptor
                                http-server.metric/registry-metric!]
              :route-name :registry-metric]
             ["/metrics" :get [service.interceptors/http-request-in-handle-timing-interceptor
                               component.prometheus/expose-metrics-http-request-handler]
              :route-name :fetch-metrics]])
