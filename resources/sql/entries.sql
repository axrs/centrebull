-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (competition_id, sid, class)
VALUES (:competition-id::UUID, :sid, :class)
RETURNING *;

-- :name entries-delete! :! :n
-- :doc Deletes an entry
DELETE FROM entries
WHERE id = :id::UUID;
