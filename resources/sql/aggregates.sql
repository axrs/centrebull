-- :name aggregates-create! :<! :1
-- :doc create an aggregate
INSERT INTO aggregates
(name, priority, competition_id, activities)
VALUES (:name, :priority, :competition-id :activities)
RETURNING *;