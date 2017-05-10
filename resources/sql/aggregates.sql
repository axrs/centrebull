-- :name aggregates-create! :<! :1
-- :doc create an aggregate
INSERT INTO aggregates
(description, priority, competition_id, activities)
VALUES (:description, :priority, :competition-id::UUID, :activities::UUID [])
RETURNING *;

-- :name aggregates-find :? :*
-- :doc finds aggregates for a competition
SELECT *
FROM aggregates
WHERE competition_id = :competition-id::UUID;

-- :name aggregates-delete!  :! :n
-- :doc deletes an aggregate for a competition
DELETE
FROM aggregates
WHERE id = :id::UUID AND competition_id = :competition-id::UUID;

-- :name aggregates-find-results :? :*
-- :doc finds all results for an aggregate
SELECT
  class,
  r.sid,
  first_name,
  last_name,
  club,
  priority,
  score,
  vs,
  activity_id,
  description
FROM results r
  LEFT JOIN shooters s ON s.sid = r.sid
  LEFT JOIN activities a ON a.id = r.activity_id
  LEFT JOIN ranges g ON a.range_id = g.id
  LEFT JOIN entries e ON e.sid = r.sid
WHERE activity_id IN (SELECT unnest(activities)
                      FROM aggregates
                      WHERE id = :id::UUID AND
                            competition_id = :competition-id::UUID)
ORDER BY class, sid, priority ASC;

-- :name grand-aggregates-create! :<! :1
-- :doc creates a grand
INSERT INTO grand_aggregates (description, priority, competition_id, aggregates)
VALUES (:description, :priority, :competition-id::UUID, :aggregates::UUID [])
RETURNING *;

-- :name grand-aggregates-find :? :*
-- :doc finds grand aggregates for a competition
SELECT *
FROM grand_aggregates
WHERE competition_id = :competition-id::UUID;

-- :name grand-aggregates-find-by-id :? :*
-- :doc Finds a shooter by an id
SELECT *
FROM grand_aggregates
WHERE id = :id::UUID AND competition_id = :competition-id::UUID
LIMIT 1;

-- :name grand-aggregates-delete!  :! :n
-- :doc deletes a grand aggregate for a competition
DELETE
FROM grand_aggregates
WHERE id = :id::UUID AND competition_id = :competition-id::UUID;
