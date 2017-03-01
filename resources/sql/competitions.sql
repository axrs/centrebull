-- :name competitions-create! :! :n
-- :doc Creates a new competition
INSERT INTO competitions (description, start_date, end_date)
VALUES (:description, :start-date::DATE, :end-date::DATE)
RETURNING *;
