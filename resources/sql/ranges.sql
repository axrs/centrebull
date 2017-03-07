-- :name ranges-create! :<! :1
-- :doc create a range record
INSERT INTO ranges
(description)
VALUES (:description)
RETURNING *;
