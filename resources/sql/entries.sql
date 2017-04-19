-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (competition_id, sid, class)
VALUES (:competition-id::UUID, :sid, :class)
RETURNING *;

-- :name entries-withdraw! :! :n
-- :doc Withdraws a shooters entry into a competition
UPDATE entries
SET active = FALSE
WHERE id = :id::UUID;
