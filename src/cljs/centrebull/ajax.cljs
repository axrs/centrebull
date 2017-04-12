(ns centrebull.ajax
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]))

(defn local-uri? [{:keys [uri]}]
  (and (string? uri) (not (re-find #"^\w+?://" uri))))

(defn default-headers [request]
  (if (local-uri? request)
    (-> request
        (update :uri #(str js/context %)))
    ;(update :headers #(merge {"Authorization" (str "Token " @(rf/subscribe [:auth-token]))})))
    request))

(defn empty-means-nil [response]
  (if (ajax.protocols/-body response)
    response
    (reduced [(-> response ajax.protocols/-status ajax.core/success?) nil])))

(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name    "default headers"
                               :request default-headers})
         (ajax/to-interceptor {:name     "JSON special case nil"
                               :response empty-means-nil})))

(declare clj->jskw)

(defn key->jskw [k]
  (if (satisfies? IEncodeJS k)
    (-clj->js k)
    (if (or (string? k)
            (number? k)
            (keyword? k)
            (symbol? k))
      (clj->jskw k)
      (pr-str k))))

(defn clj->jskw
  "Note: Altered from cljs.core to encode keywords
  Recursively transforms ClojureScript values to JavaScript.
  sets/vectors/lists become Arrays, Keywords and Symbol become Strings,
  Maps become Objects. Arbitrary keys are encoded to by key->js."
  [x]
  (when-not (nil? x)
    (if (satisfies? IEncodeJS x)
      (-clj->js x)
      (cond
        (keyword? x) (string/replace (str (keyword x)) #"^:" "")
        (symbol? x) (str x)
        (map? x) (let [m (js-obj)]
                   (doseq [[k v] x]
                     (aset m (key->jskw k) (clj->js v)))
                   m)
        (coll? x) (let [arr (array)]
                    (doseq [x (map clj->js x)]
                      (.push arr x))
                    arr)
        :else x))))

(defn- append-result
  "Appends the HTTP result object to the end of all after requests"
  [result afters]
  (into [] (map #(conj % result) afters)))

(rf/reg-event-fx
  ::good-http-result
  (fn [db [_ errors after-success result]]
    (if (instance? reagent.ratom/RAtom errors)
      (reset! errors nil))
    {:dispatch-n (append-result result after-success)}))

(rf/reg-event-fx
  ::bad-http-result
  (fn [db [_ errors after-errors result]]
    (if (instance? reagent.ratom/RAtom errors)
      (reset! errors (get-in result [:response :errors])))
    {:dispatch-n (append-result result after-errors)}))

(defn get-json
  "Issues a GET request to a specified URL, dispatching success or error handlers respectively"
  [{:keys [url after-success after-errors]
    :or   {after-success [] after-errors []}
    :as   request}]
  {:http-xhrio {:method          :get
                :uri             url
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [::good-http-result nil after-success]
                :on-failure      [::bad-http-result nil after-errors]}})

(defn delete-json
  "Issues a DELETE request to a specified URL, dispatching success or error handlers respectively"
  [{:keys [url after-success after-errors]
    :or   {after-success [] after-errors []}
    :as   request}]
  {:http-xhrio {:method          :delete
                :uri             url
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [::good-http-result nil after-success]
                :on-failure      [::bad-http-result nil after-errors]}})

(defn post-json
  "Issues a POST request to a specified URL, sending a provided body and dispatching success or
  error handlers respectively"
  [{:keys [url body errors after-success after-errors]
    :or   {body nil errors nil after-success [] after-errors []}
    :as   request}]
  {:http-xhrio {:method          :post
                :uri             url
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :params          (clj->jskw body)
                :on-success      [::good-http-result errors after-success]
                :on-failure      [::bad-http-result errors after-errors]}})

(defn put-json
  "Issues a PUT request to a specified URL, sending a provided body and dispatching success or
  error handlers respectively"
  [{:keys [url body errors after-success after-errors]
    :or   {body nil errors nil after-success [] after-errors []}
    :as   request}]
  {:http-xhrio {:method          :put
                :uri             url
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :params          (clj->jskw body)
                :on-success      [::good-http-result errors after-success]
                :on-failure      [::bad-http-result errors after-errors]}})
