-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (id, side, grade)
VALUES (:id, :sid, :grade);
