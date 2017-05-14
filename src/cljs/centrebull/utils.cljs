(ns centrebull.utils
  (:require [goog.string :as gstring]
            [goog.string.format]))

(def sort-format (partial gstring/format "%04d"))

(defn find-max-priorities [results]
  (->> results (map :aggregate/priority) distinct set last))

(defn- translate-grade [g]
  (cond
    (= "A" g) "10"
    (= "B" g) "09"
    (= "C" g) "08"
    (= "D" g) "07"
    (= "FS" g) "06"
    (= "FS1" g) "05"
    (= "FS2" g) "04"
    (= "FO" g) "03"
    (= "FO1" g) "02"
    (= "FO2" g) "01"
    :else "00"))

(defn- calc-sort-str [max results]
  (loop [p max
         return []]
    (if (not= -1 p)
      (let [r (first (filter #(= p (:aggregate/priority %)) results))]
        (recur (dec p) (concat return [(sort-format (or (:result/score r) 0)) (sort-format (or (:result/vs r) 0))])))
      return)))

(defn update-shooter [max-priorities]
  (fn [[sid v]]
    (let [r (first v)
          g (:shooter/grade r)
          results (->> v (mapv #(select-keys % [:result/score :result/vs :aggregate/priority])))
          score (->> v (map :result/score) (apply +))
          vs (->> v (map :result/vs) (apply +))
          sort-key (calc-sort-str max-priorities results)]
      (-> r
        (dissoc :activity/id :aggregate/description :result/score :result/vs)
        (assoc :aggregate/score score :aggregate/vs vs :aggregate/results results)
        (assoc :sort-key (str (translate-grade g)
                           (sort-format (or score 0))
                           (sort-format (or vs 0))
                           (apply str sort-key)))))))

(defn rank-results
  "Ranks results based on grade"
  [results]
  (let [grouped-results (group-by :shooter/grade results)
        ranked-grouped-results (map (fn [[grade group]] (map-indexed #(assoc %2 :rank (inc %1)) group)) grouped-results)
        merged-ranked-grouped-results (apply concat ranked-grouped-results)]
   merged-ranked-grouped-results))

(defn sorted-results
  "Sorts shooter results based on :sort-key"
  [results]
  (let [h (find-max-priorities results)
          pred (update-shooter h)
          grouped (group-by :shooter/sid results)
          results (->> grouped
                    (map pred)
                    (sort-by :sort-key >))]
   results))
