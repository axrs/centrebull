-- :name activities-create! :<! :1
-- :doc creates a new activity record
INSERT INTO activities (competition_id, range_id, priority, date)
VALUES (:competition-id, :range-id, :priority, :date::DATE);
