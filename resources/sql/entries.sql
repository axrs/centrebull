-- :name entries-create! :<! :1
-- :doc Creates a new entry
INSERT INTO entries (competition_id, sid, class)
VALUES (:competition-id::UUID, :sid, :class) RETURNING *;

-- :name entries-update-active! :! :n
-- :doc updates an entries active column
UPDATE entries
SET active = :active
WHERE id = :id::UUID;

-- :name entries-find :<! :1
-- :doc Finds an entry
SELECT *
FROM entries
WHERE competition_id = :competition-id::UUID AND sid = :sid;

-- :name competitions-retrieve-registrations :? :n
-- :doc Retrieves all registrations for a competition
SELECT entries.*, shooters.*, results.*
FROM entries
  JOIN shooters
    ON entries.sid = shooters.sid
  LEFT JOIN activities ON activities.competition_id = entries.competition_id
  LEFT JOIN results ON results.activity_id = activities.id
WHERE entries.active = TRUE AND entries.competition_id = :competition-id::UUID
      AND :activity-id = activities.id
ORDER BY entries.class DESC, results.score DESC, results.vs DESC, results.shots_mirror DESC;