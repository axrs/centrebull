-- :name activities-create! :<! :1
-- :doc creates a new activity record
INSERT INTO activities (competition_id, range_id, priority, date)
VALUES (:competition-id::UUID, :range-id, :priority, :date::DATE)
RETURNING *;

-- :name activities-delete! :! :n
-- :doc Deletes an activity with a given id
DELETE
FROM activities
WHERE id = :id::UUID;