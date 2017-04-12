-- :name ranges-create! :<! :1
-- :doc create a range record
INSERT INTO ranges
(description)
VALUES (:description)
RETURNING *;

-- :name ranges-delete! :! :n
-- :doc delete a range record
DELETE
FROM ranges
WHERE id = :id::UUID;

-- :name ranges-suggest :? :*
-- :require [clojure.string :as string]
-- :doc Suggests ranges for given search terms
SELECT *
FROM ranges
WHERE /*~ (string/join " AND " (for [value params] (str "description ILIKE '" value "'"))) ~*/
ORDER BY description ASC
LIMIT 25;
