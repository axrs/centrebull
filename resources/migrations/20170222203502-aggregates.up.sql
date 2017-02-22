CREATE TABLE aggregates (
  id          UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
  description TEXT             NOT NULL,
  activities  UUID REFERENCES activities (id)
)
