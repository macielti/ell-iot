(ns ell-iot.diplomat.http-server.metric
  (:require [schema.core :as s]
            [ell-iot.adapters.metric :as adapters.metric]
            [ell-iot.controllers.metric :as controllers.metric]))

(s/defn registry-metric!
  [{body                        :json-params
    {:keys [prometheus config]} :components}]
  (-> (adapters.metric/wire->internal body)
      (controllers.metric/registry-metric! prometheus config))
  {:status 202
   :body   ""})
