-- :name results-create! :<! :1
-- :doc creates a results record
INSERT INTO results
(sid, activity_id, shots, score, vs)
  VALUES (:sid, :activity-id, :shots, :score, :vs)
RETURNING *;
