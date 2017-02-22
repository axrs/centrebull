CREATE TABLE activities (
  id             UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
  competition_id UUID REFERENCES competitions (id),
  range_id       UUID REFERENCES ranges (id),
  priority       INT                       DEFAULT 1,
  date           TIMESTAMP        NOT NULL
)
