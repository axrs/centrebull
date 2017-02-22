(ns centrebull.test.routes.specify
  (:require
    [clojure.test :refer [deftest testing is]]
    [ring.util.io :refer [string-input-stream]]
    [centrebull.routes.specify :as middleware]))

(def ^:private replace-in-key #'middleware/replace-in-key)

(deftest test-replace-in-key
  (testing "Should replace contents if the first argument is a string"
    (is (= (replace-in-key :this #"--" "/") "this"))
    (is (= (replace-in-key :this/user #"--" "/") "this/user"))
    (is (= (replace-in-key "template--id" #"--" "/") "template/id"))
    (is (= (replace-in-key :this/user--name #"--" "/") "this/user/name"))
    (is (= (replace-in-key "This parrot is no more!" #" no more" "") "This parrot is!"))))

(def ^:private replace-ddash-slash #'middleware/replace-ddash-slash)
(deftest test-replace-ddash-slash
  (testing "Should replace contents of a map key"
    (let [input (first {:template--id "Nobody expects the spanish Inquisition!"})
          expected {"template/id" "Nobody expects the spanish Inquisition!"}]
      (is (= (replace-ddash-slash input) expected)))))

(def ^:private convert-string-keys #'middleware/convert-string-keys)
(deftest test-convert-string-keys
  (testing "Should convert string keys to keywords (replacing double dashes)"
    (let [input {"Terry--Jones" "Spam! Spam! Spam! Spam! Spam! Spam!"
                 "Palin"        "I put on women's clothing and hang around in bars"
                 :John/Cleese   "Venezuelan beaver cheese?"}
          expected {:Terry/Jones "Spam! Spam! Spam! Spam! Spam! Spam!"
                    :Palin       "I put on women's clothing and hang around in bars"
                    :John/Cleese "Venezuelan beaver cheese?"}]
      (is (= (convert-string-keys input) expected)))))

(deftest test-specify-nil-spec
  (let [handler ((middleware/specify nil) (fn [r] r))]
    (testing "Should run spec over a request, keywording params"
      (let [value {:foo "bar" :uri "http://montypython.com"}
            request {:headers {"content-type" "application/json; charset=UTF-8"}
                     :body    {:ignored-field "this"}
                     :params  {"Michael--Palin" "It's…" "John-Cleese" "My hovercraft is full of eels."}}
            response (handler request)]
        (prn response)
        (is (= (:body response) (:body request)))
        (is (= (:params response) {:Michael/Palin "It's…" :John-Cleese "My hovercraft is full of eels."}))))))

