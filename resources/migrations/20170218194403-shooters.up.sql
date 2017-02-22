CREATE TABLE shooters (
  sid            NUMERIC PRIMARY KEY,
  last_name      TEXT NOT NULL,
  first_name     TEXT NOT NULL,
  preferred_name TEXT,
  club           TEXT
)
