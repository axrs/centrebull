CREATE TABLE grand_aggregates (
  id             UUID PRIMARY KEY                 NOT NULL DEFAULT gen_random_uuid(),
  description    TEXT                             NOT NULL,
  aggregates     UUID []                          NOT NULL,
  priority       NUMERIC                          NOT NULL,
  competition_id UUID REFERENCES competitions (id)NOT NULL
)
