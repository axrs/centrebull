CREATE TABLE activities (
  id             UUID PRIMARY KEY,
  competition_id UUID REFERENCES competitions (id),
  range_id       UUID REFERENCES ranges (id),
  priority       INT DEFAULT 1,
  date           DATE NOT NULL
)
