#!/bin/bash
set -e
createuser -s liquidbyte

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE liquidbyte_dev ENCODING 'UTF-8';
    GRANT ALL PRIVILEGES ON DATABASE liquidbyte_dev to liquidbyte;

    CREATE DATABASE liquidbyte_test ENCODING 'UTF-8';
    GRANT ALL PRIVILEGES ON DATABASE liquidbyte_test to liquidbyte;
EOSQL
