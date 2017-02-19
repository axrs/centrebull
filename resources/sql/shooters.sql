-- :name shooters-create! :! :n
-- :doc creates a new shooter record
INSERT INTO shooters
(sid, first_name, last_name, preferred_name, club)
VALUES (:sid, :first-name, :last-name, :preferred-name, :club);

-- :name shooters-suggest :? :*
-- :require [clojure.string :as string]
-- :doc Suggests shooters for given search terms
SELECT *
FROM shooters
WHERE /*~ (string/join " AND " (for [value params] (str "suggest ILIKE '" value "'"))) ~*/
ORDER BY sid ASC, first_name ASC, last_name ASC
LIMIT 25;