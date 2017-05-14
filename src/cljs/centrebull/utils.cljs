(ns centrebull.utils)

(defn rank-results
  "Ranks results based on grade"
  [results]
  (let [grouped-results (group-by :shooter/grade results)
        ranked-grouped-results (map (fn [[grade group]] (map-indexed #(assoc %2 :rank (inc %1)) group)) grouped-results)
        merged-ranked-grouped-results (apply concat ranked-grouped-results)]
   merged-ranked-grouped-results))
