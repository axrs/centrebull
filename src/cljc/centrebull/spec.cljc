(ns centrebull.spec
  #?(:clj
     (:import java.util.UUID))
  (:require
    [clojure.spec :as s]
    [clojure.walk :refer [postwalk]]
    [clojure.string :as string]
    [clojure.tools.reader :refer [read-string]]))

; Spec error mapping for human readable messages
(def ^:private explain {:api/shooter-create {:shooter/first-name "A first name is required."
                                             :shooter/last-name  "A last name is requried."}
                        :api/ranges-create  {:range/description "A description is required."}})

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
(defn- uuid-str? [s] (and (string? s) (re-matches #"(\w{8}(-\w{4}){3}-\w{12}?)$" s)))

(defn- ->uuid
  "clojure.spec conformer function to convert the argument
  into a UUID type"
  [s]
  (cond
    (uuid? s) s
    (keyword? s) (->uuid (name s))
    (uuid-str? s) #?(:clj  (UUID/fromString s)
                     :cljs (UUID. s nil))
    :else ::s/invalid))

(defn- shot-length-10-15
  "Ensures the shots are either 10 or 15 shot matches (with 2 optional siders)"
  [s]
  (not (nil? (some #{(count s)} [10 11 12 15 16 17]))))

(defn- valid-shot-chars-only
  "Ensures the shots are either -0123456V or X"
  [s]
  (empty? (filter #(not (some #{%} "-0123456VX")) s)))

(defn- shot->int [v]
  (case v
    \X 10
    \6 6
    \V 5
    \5 5
    \4 4
    \3 3
    \2 2
    \1 1
    0))

(defn- last-required-shots [s]
  (let [s (string/reverse s)
        c (count s)
        len (if (< 12 c) 15 10)]
    (->> len
      (min c)
      (subs s 0))))

(defn- calculate-score [s]
  (->> s
    last-required-shots
    (map shot->int)
    (apply +)))

(defn- calculate-vs [s]
  (->> s
    last-required-shots
    (filter #(= \V %))
    count))

(defn- calculate-result [{:keys [result/shots]
                          :or   {shots ""}
                          :as   m}]
  (merge m {:result/score (calculate-score shots) :result/vs (calculate-vs shots)}))

;Clojure spec predicate for a non empty string
(def non-empty-string (s/and string? #(not= "" %)))

(defn- str->int [s] #?(:cljs (cond (and (string? s) #(not= "" %)) (read-string s)
                                   (integer? s) s
                                   :else s)
                       :clj  (cond (and (string? s) #(not= "" %))
                                   (let [t (clojure.tools.reader/read-string s)]
                                     (if (integer? t) t ::s/invalid))
                                   (integer? s) s
                                   :else ::s/invalid)))

(def ^:private is-date? (s/and string? #(re-matches #"^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$" %)))
(def ^:private is-integer (s/conformer str->int))

(s/def :shooter/sid is-integer)
(s/def :shooter/first-name non-empty-string)
(s/def :shooter/last-name non-empty-string)
(s/def :shooter/preferred-name string?)
(s/def :shooter/club string?)
(s/def :shooter/grade string?)

(s/def :range/id (s/conformer ->uuid))
(s/def :range/description non-empty-string)

(s/def :competition/id (s/conformer ->uuid))
(s/def :competition/description non-empty-string)
(s/def :competition/start-date is-date?)
(s/def :competition/end-date is-date?)

(s/def :activity/id (s/conformer ->uuid))
(s/def :activity/range-id (s/conformer ->uuid))
(s/def :activity/priority is-integer)
(s/def :activity/date is-date?)

(s/def :search/q string?)

(s/def :result/id (s/conformer ->uuid))
(s/def :result/shots (s/and string?
                       shot-length-10-15
                       valid-shot-chars-only))

(s/def :entry/id (s/conformer ->uuid))

(s/def :aggregate/activities (s/coll-of ->uuid :min-count 2))
(s/def :aggregate/description string?)
(s/def :aggregate/priority integer?)
(s/def :aggregate/id (s/conformer ->uuid))

;----------------------------------------
; API END POINTS
;----------------------------------------

(s/def :api/shooter-create
  (s/keys
    :req [:shooter/sid
          :shooter/last-name
          :shooter/first-name]
    :opt [:shooter/preferred-name
          :shooter/club]))

(s/def :api/shooter-id-only
  (s/keys
    :req [:shooter/sid]))

(s/def :api/range-id-only
  (s/keys
    :req [:range/id]))

(s/def :api/ranges-create
  (s/keys
    :req [:range/description]))

(s/def :api/competition-id-only
  (s/keys
    :req [:competition/id]))

(s/def :api/competition-and-activity-id-only
  (s/keys
    :req [:competition/id
          :activity/id]))

(s/def :api/search (s/keys :req [:search/q]))

(s/def :api/competition-create
  (s/keys
    :req [:competition/description
          :competition/start-date
          :competition/end-date]))

(s/def :api/competition-register-shooter
  (s/keys
    :req [:competition/id
          :shooter/sid
          :shooter/grade]))

(s/def :api/competition-unregister-shooter
  (s/keys
    :req [:entry/id]))

(s/def :api/competition-suggest-registration
  (s/keys
    :req [:competition/id
          :search/q]))

(s/def :api/activity-id-only
  (s/keys
    :req [:activity/id]))

(s/def :api/activity-create
  (s/keys
    :req [:activity/date
          :competition/id
          :activity/range-id
          :activity/priority]))

(s/def :api/ranges-suggest
  (s/keys
    :req [:search/q]))

(s/def :api/result-create
  (s/and
    (s/keys :req [:result/shots :activity/id :shooter/sid])
    (s/conformer calculate-result)))

(s/def :api/aggregate-create
  (s/keys
    :req [:aggregate/activities
          :aggregate/description
          :aggregate/priority
          :competition/id]))

(s/def :api/delete-aggregate
  (s/keys
    :req [:aggregate/id
          :competition/id]))
