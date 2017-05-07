ALTER TABLE results
ADD CONSTRAINT sid_activity_id_unique UNIQUE (sid, activity_id);