(ns ell-iot.controllers.metric
  (:require [ell-iot.models.metric :as models.metric]
            [iapetos.core :as prometheus]
            [medley.core :as medley]
            [prometheus-component.core :as component.prometheus]
            [schema.core :as s]))

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

(s/defmethod registry-metric! :gauge
  [metric :- models.metric/RegistryMetric
   prometheus
   _config]
  (cond (:labels metric) (prometheus/set (:registry prometheus) (:name metric) (:labels metric) (:value metric))
        :else (prometheus/set (:registry prometheus) (:name metric) (:value metric))))

(s/defmethod registry-metric! :summary
  [metric :- models.metric/RegistryMetric
   prometheus
   _config]
  (cond (:labels metric) (prometheus/observe (:registry prometheus) (:name metric) (:labels metric) (:value metric))
        :else (prometheus/observe (:registry prometheus) (:name metric) (:value metric))))

(s/defmethod registry-metric! :histogram
  [metric :- models.metric/RegistryMetric
   prometheus
   _config]
  (cond (:labels metric) (prometheus/observe (:registry prometheus) (:name metric) (:labels metric) (:value metric))
        :else (prometheus/observe (:registry prometheus) (:name metric) (:value metric))))
