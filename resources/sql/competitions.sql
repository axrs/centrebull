-- :name competitions-create! :<! :1
-- :doc Creates a new competition
INSERT INTO competitions (description, start_date, end_date)
VALUES (:description, :start-date::DATE, :end-date::DATE)
RETURNING *;

-- :name competitions-find :? :1
-- :doc Finds a single competition with a given id
SELECT *
FROM competitions
WHERE id = :id::UUID;

-- :name competitions-delete! :! :n
-- :doc Deletes a competition with a given id
DELETE
FROM competitions
WHERE id = :id::UUID;

-- :name competitions-suggest :? :*
-- :require [clojure.string :as string]
-- :doc Suggests competitions for given search terms
SELECT *
FROM competitions
WHERE /*~ (string/join " AND " (for [value params] (str "suggest ILIKE '" value "'"))) ~*/
ORDER BY id ASC, description ASC, start_date ASC, end_date ASC
LIMIT 25;

-- :name competitions-suggest-registration :? :*
-- :require [clojure.string :as string]
-- :doc Suggests shooters for given search terms and whether or not they're registered in supplied competition
SELECT *
FROM shooters
LEFT JOIN entries
  ON shooters.sid = entries.sid
  AND competition_id = :id::UUID
  AND entries.active = TRUE
WHERE /*~ (string/join " AND " (for [value (:s params)] (str "shooters.suggest ILIKE '" value "'"))) ~*/
ORDER BY shooters.sid ASC, shooters.first_name ASC, shooters.last_name ASC
LIMIT 25;
