(ns centrebull.spec
  (:import (java.net URL))
  #?(:clj
     (:require
       [clojure.spec :as s]
       [clojure.walk :refer [postwalk]]
       [clojure.string :as string]))
  #?(:cljs
     (:require
       [cljs.spec :as s]
       [clojure.walk :refer [postwalk]]
       [cljs.string :as string])))

; Spec error mapping for human readable messages
(def ^:private explain {::shooter-create {:shooter/first-name "A first name is required."
                                          :shooter/last-name  "A last name is requried."}
                        ::ranges-create  {:range/description "A description is required."}})



;----------------------------------------
; SPEC VALIDATOR AND EXPLAIN
;----------------------------------------
(defn- get-invalid-field
  "Determines the invalid field of a given map structure."
  [{:keys [path via pred]}]
  (cond
    (and (seq? pred)
      (s/get-spec (last pred))) (merge via (last pred))
    :else via))

(defn- explain-error [v]
  (or (get-in explain v) "__No error message found.__"))

(defn- assoc-errors [e]
  "Associates errors with their human readable description"
  (->> e
    explain-error
    (assoc-in {} e)
    first
    val))

(defn- explain-spec-errors [errors]
  "Finds the human readable message for each error and produces a single map
  of all errors"
  (let [errs (map get-invalid-field errors)]
    (apply merge (map assoc-errors errs))))

(defn- find-problems [spec m]
  (::s/problems (s/explain-data spec m)))

(defn- pretty-format-spec [spec m]
  (let [x
        (->> m
          (find-problems spec)
          explain-spec-errors)]
    x))

(defn validate-spec
  "Validates a spec against a provided map, returning a list of problems or nil"
  [spec m]
  (if (not (s/valid? spec m))
    (pretty-format-spec spec m)
    nil))

;----------------------------------------
; SPEC DEFINITIONS
;----------------------------------------
(defn- str->uuid [s] (java.util.UUID/fromString s))

(def ^:private is-uuid? (s/and string? #(re-matches #"(\w{8}(-\w{4}){3}-\w{12}?)$" %) (s/conformer str->uuid)))
(def ^:private non-empty-string (s/and string? #(not= "" %)))
(def ^:private is-date? (s/and string? #(re-matches #"^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$" %)))

(s/def :shooter/sid number?)
(s/def :shooter/first-name non-empty-string)
(s/def :shooter/last-name non-empty-string)
(s/def :shooter/preferred-name string?)
(s/def :shooter/club string?)

(s/def :range/description non-empty-string)

(s/def :competition/id is-uuid?)
(s/def :competition/description non-empty-string)
(s/def :competition/start-date is-date?)
(s/def :competition/end-date is-date?)
;----------------------------------------
; API END POINTS
;----------------------------------------

(s/def ::shooter-create
  (s/keys
    :req [:shooter/sid
          :shooter/last-name
          :shooter/first-name]
    :opt [:shooter/preferred-name
          :shooter/club]))

(s/def ::ranges-create
  (s/keys
    :req [:range/description]))

(s/def ::competition-id-only
  (s/keys
    :req [:competition/id]))

(s/def ::competition-create
  (s/keys
    :req [:competition/description
          :competition/start-date
          :competition/end-date]))
