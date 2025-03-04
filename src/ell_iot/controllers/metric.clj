(ns ell-iot.controllers.metric
  (:require [ell-iot.models.metric :as models.metric]
            [iapetos.core :as prometheus]
            [schema.core :as s]
            [medley.core :as medley]
            [prometheus-component.core :as component.prometheus]))

(s/defn metric->type :- component.prometheus/MetricType
  [metric-name :- s/Keyword
   config]
  (let [metrics (->> config :metrics (medley/index-by :name))]
    (get-in metrics [metric-name :type])))

(defmulti registry-metric!
  (fn [metric _prometheus config] (metric->type (:name metric) config)))

(s/defmethod registry-metric! :counter
  [metric :- models.metric/RegistryMetric
   prometheus
   _config]
  (cond (:labels metric) (prometheus/inc (:registry prometheus) (:name metric) (:labels metric))
        :else (prometheus/inc (:registry prometheus) (:name metric))))
