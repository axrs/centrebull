-- :name competitions-create! :<! :1
-- :doc Creates a new competition
INSERT INTO competitions (description, start_date, end_date)
VALUES (:description, :start-date::DATE, :end-date::DATE)
RETURNING *;

-- :name competitions-find :? :1
-- :doc Finds a single competition with a given id
SELECT *
FROM competitions
WHERE id = :id::UUID;
