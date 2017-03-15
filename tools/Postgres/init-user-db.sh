#!/bin/bash
set -e
createuser -s centrebull

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE centrebull_dev ENCODING 'UTF-8';
    GRANT ALL PRIVILEGES ON DATABASE centrebull_dev to centrebull;

    CREATE DATABASE centrebull_test ENCODING 'UTF-8';
    GRANT ALL PRIVILEGES ON DATABASE centrebull_test centrebull;
EOSQL
