-- :name shooters-create! :<! :1
-- :doc creates a new shooter record
INSERT INTO shooters
(sid, first_name, last_name, preferred_name, club)
VALUES (:sid, :first-name, :last-name, :preferred-name, :club)
RETURNING *;

-- :name shooters-suggest :? :*
-- :require [clojure.string :as string]
-- :doc Suggests shooters for given search terms
SELECT *
FROM shooters
WHERE /*~ (string/join " AND " (for [value params] (str "suggest ILIKE '" value "'"))) ~*/
ORDER BY first_name ASC, last_name ASC
LIMIT 25;

-- :name shooters-find-by-id :? :*
-- :doc Finds a shooter by an id
SELECT *
FROM shooters
WHERE sid = :sid
LIMIT 1;
