(ns ell-iot.diplomat.http-server.metric
  (:require [ell-iot.adapters.metric :as adapters.metric]
            [ell-iot.controllers.metric :as controllers.metric]
            [schema.core :as s]))

(s/defn registry-metric!
  [{body                        :json-params
    {:keys [prometheus config]} :components}]
  (-> (adapters.metric/wire->internal body)
      (controllers.metric/registry-metric! prometheus config))
  {:status 202
   :body   ""})
