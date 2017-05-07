ALTER TABLE aggregates ADD COLUMN priority NUMERIC NOT NULL;
ALTER TABLE aggregates ADD COLUMN competition_id UUID REFERENCES competitions (id) NOT NULL;