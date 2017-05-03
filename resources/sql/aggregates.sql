-- :name aggregates-create! :<! :1
-- :doc create an aggregate
INSERT INTO aggregates
(description, priority, competition_id, activities)
VALUES (:description, :priority, :competition-id::UUID, :activities::UUID[])
RETURNING *;