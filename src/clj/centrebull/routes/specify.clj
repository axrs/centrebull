(ns centrebull.routes.specify
  (:require [ring.util.http-response :as response]
            [compojure.api.meta :refer [restructure-param]]
            [clojure.string :as str]
            [clojure.spec :as s]
            [centrebull.spec :refer [validate-spec]]
            [clojure.set :refer [difference]]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.tools.logging :as log]))

(defn- replace-in-key [s f r]
  (cond
    (string? s) (str/replace s f r)
    (keyword? s) (-> s
                     keyword
                     str
                     (str/replace f r)
                     (str/replace #"^:" ""))
    :else s))

(defn- replace-ddash-slash
  "Replaces the `--` in the key to a `/` if the keyword is a string"
  [m]
  (hash-map (replace-in-key (key m) #"--" "/") (val m)))

(defn- convert-string-keys [params]
  (->> params
       (map #(replace-ddash-slash %))
       (into {})
       keywordize-keys))

(defn- dissoc-keys
  "Removes keys and values from a map which are not shared with the provided spec"
  [spec m]
  (prn spec)
  (if spec
    (let [k (distinct (apply concat (filter #(vector? %) (s/describe spec))))
          ik (difference (set (keys m)) (set k))]
      (apply dissoc m ik))
    m))

(defn validate
  "Validates an incoming endpoint request against a spec, throwing a bad request if invalid"
  [spec params]
  (let [errors (if spec (validate-spec spec params))]
    (if errors (response/bad-request! {:errors errors}))
    params))

(defn conform
  [spec params]
  (if spec (s/conform spec params) params))

(defn specify [spec]
  (log/info "Wrapping route in spec validation " spec)
  (fn [h]
    (fn [{body :body-params query :query-params route :route-params :as r}]
      (->> (merge body query route)
           convert-string-keys
           (validate spec)
           (dissoc-keys spec)
           (conform spec)
           (assoc r :all-params)
           h))))

(defmethod restructure-param :spec [_ spec acc]
  (-> acc
      (update-in [:middleware] conj [`(specify ~spec)])))
