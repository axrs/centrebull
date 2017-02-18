#!/bin/bash

# add the PostgreSQL repository to the list
cat <<EOF > /tmp/pgdg.list
deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main
EOF

# Default to UTF-8
cat <<EOF > /tmp/locale
LANG="en_US.UTF-8"
EOF

mv /tmp/pgdg.list /etc/apt/sources.list.d/pgdg.list
mv /tmp/locale /etc/default/locale

# install PostgreSQL with Postgis
wget -q -O - http://apt.postgresql.org/pub/repos/apt/ACCC4CF8.asc | apt-key add -
apt-get update
apt-get -y install postgresql-9.5-postgis-2.2 postgresql-contrib-9.5 postgresql-client-9.5 pgadmin3

service postgresql restart

# setup appropriate access rules
su postgres -c "psql -c \"ALTER USER postgres PASSWORD 'postgres';\""

cat <<EOF2 > /etc/postgresql/9.5/main/pg_hba.conf
# TYPE  DATABASE        USER            ADDRESS        METHOD
local   all             postgres                        peer
# "local" is for Unix domain socket connections only
local   all             all                             md5
# IPv4 local connections:
host    all             all             0.0.0.0/0       md5
# IPv6 local connections:
host    all             all             ::1/128         md5
EOF2

sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/9.5/main/postgresql.conf

service postgresql restart

su - postgres -c "createuser -s centrebull"
su - postgres -c "psql -c \"ALTER ROLE centrebull PASSWORD 'manager'\""
su - postgres -c "createdb -O centrebull -EUTF-8 centrebull_dev"
su - postgres -c "createdb -O centrebull -EUTF-8 centrebull_dev"
