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

-- :name activities-find-for-competition :? :*
-- :doc Finds all activities for a given competition id
SELECT a.*, r.*
FROM activities a
LEFT JOIN ranges r ON a.range_id = r.id
WHERE competition_id = :competition-id::UUID
ORDER BY date ASC, priority ASC;

-- :name activities-find-for-competition-and-in-coll :? :*
-- :doc Finds all activities for a competition where activity-id exists in collection
SELECT *
FROM activities
WHERE activities.competition_id = :competition-id::UUID AND activities.id = ANY (:activities::UUID[])