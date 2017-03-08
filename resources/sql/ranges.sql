-- :name ranges-create! :<! :1
-- :doc create a range record
INSERT INTO ranges
(description)
VALUES (:description)
RETURNING *;

-- :name ranges-delete! :! :n
-- :doc delete a range record
DELETE
FROM ranges
WHERE id = :id::UUID;
