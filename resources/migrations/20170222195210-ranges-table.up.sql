CREATE TABLE ranges (
  id          UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
  description TEXT             NOT NULL
)
