CREATE TABLE results (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    shooter_id NUMERIC REFERENCES shooters (sid),
    activity_id UUID REFERENCES activities (id),
    shots TEXT NOT NULL,
    score INT NOT NULL,
    vs INT NOT NULL
)
