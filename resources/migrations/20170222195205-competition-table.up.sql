CREATE TABLE competitions (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid()
    description TEXT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL
)