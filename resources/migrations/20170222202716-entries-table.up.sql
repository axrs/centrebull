CREATE TABLE entries (
  id             UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
  sid            NUMERIC REFERENCES shooters (sid),
  competition_id UUID REFERENCES competitions (id),
  class          TEXT             NOT NULL
)
