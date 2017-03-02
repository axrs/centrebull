-- :name ranges-create! :! :n
-- :doc create a range record
INSERT INTO ranges
(description)
VALUES (:description);

-- :name ranges-delete! :! :n
-- :doc delete a range record
DELETE
FROM ranges
WHERE id = :id::UUID;
