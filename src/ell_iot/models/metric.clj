(ns ell-iot.models.metric
  (:require [schema.core :as s]))

(def labels
  {s/Keyword s/Any})

(s/defschema Labels labels)

(def metric
  {:name                    s/Keyword
   (s/optional-key :value)  s/Num
   (s/optional-key :labels) Labels})

(s/defschema RegistryMetric metric)
