-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (competition_id, sid, class)
VALUES (:competition-id::UUID, :sid, :class) RETURNING *;

-- :name entries-update-active! :! :n
-- :doc updates an entries active column
UPDATE entries
SET active = :active
WHERE id = :id::UUID;

-- :name entries-find :<! :1
-- :doc Finds an entry
SELECT *
FROM entries
WHERE competition_id = :competition-id::UUID AND sid = :sid;
