(ns ell-iot.wire.in.metric
  (:require [schema.core :as s]))

(def labels
  {s/Str s/Any})

(s/defschema Labels labels)

(def metric
  {:name                    s/Str
   (s/optional-key :value)  s/Num
   (s/optional-key :labels) Labels})

(s/defschema RegistryMetric metric)
