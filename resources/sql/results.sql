-- :name results-create! :<! :1
-- :doc creates a results record
INSERT INTO results
(sid, activity_id, shots, score, vs)
  VALUES (:sid, :activity-id, :shots, :score, :vs)
RETURNING *;

-- :name results-update! :<! :1
-- :doc updates shots on a results record
UPDATE results
SET shots = :shots
WHERE sid = :sid AND activity_id = :activity-id
RETURNING *;

-- :name results-exist? :? :1
-- :doc checks whether results have already been added
SELECT EXISTS (SELECT *
FROM results
WHERE results.sid = :sid AND results.activity_id = :activity-id)
LIMIT 1;
