(ns ell-iot.adapters.metric
  (:require [camel-snake-kebab.core :as csk]
            [ell-iot.models.metric :as models.metric]
            [ell-iot.wire.in.metric :as wire.in.metric]
            [medley.core :as medley]
            [schema.core :as s]))

(s/defn wire-labels->internal :- models.metric/Labels
  [labels :- wire.in.metric/Labels]
  (medley/map-keys keyword labels))

(s/defn wire->internal :- models.metric/RegistryMetric
  [metric :- wire.in.metric/RegistryMetric]
  (medley/assoc-some {:name (csk/->kebab-case-keyword (:name metric))}
                     :value (:value metric)
                     :labels (some-> (:labels metric) wire-labels->internal)))
