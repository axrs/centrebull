-- :name shooters-create! :! :n
-- :doc creates a new shooter record
INSERT INTO shooters
(sid, first_name, last_name, preferred_name, club)
VALUES (:sid, :first-name, :last-name, :preferred-name, :club);