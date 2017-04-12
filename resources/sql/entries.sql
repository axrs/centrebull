-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (competition_id, sid, class)
VALUES (:competition-id::UUID, :sid, :class)
RETURNING *;
