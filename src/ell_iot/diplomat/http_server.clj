(ns ell-iot.diplomat.http-server
  (:require [prometheus-component.core :as component.prometheus]
            [service-component.interceptors :as service.interceptors]))

(def routes [["/metrics" :get [service.interceptors/http-request-in-handle-timing-interceptor
                               component.prometheus/expose-metrics-http-request-handler]
              :route-name :fetch-metrics]])
