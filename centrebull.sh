#!/bin/bash

# Enhance to use getopts to echo additional flags into the test command
txtund=$(tput sgr 0 1)          # Underline
txtbld=$(tput bold)             # Bold
grn=$(tput setaf 2)             # Green
red=$(tput setaf 1)             # Red
bldgrn=${txtbld}$(tput setaf 2) # Bold Green
bldred=${txtbld}$(tput setaf 1) # Bold Red
txtrst=$(tput sgr0)             # Reset

usage()
{
cat << EOF
${txtbld}SYNOPSIS${txtrst}
    $0 clean
    $0 reset

${txtbld}DESCRIPTION${txtrst}
    ${txtbld}clean${txtrst}
        drops all tables and runs a database ${txtbld}migration${txtrst}
    ${txtbld}reset${txtrst}
        drops all tables from the dratbase.${txtrst}
EOF
exit 1
}

echo_message() {
    echo "${bldgrn}[centrebull.sh]${txtrst} ${grn}$1${txtrst}"
}

echo_error() {
    echo "${bldred}[centrebull.sh]${txtrst} ${red}$1${txtrst}"
}

echo_line() {
    echo
    echo "${bldred}====================================================================================================>${txtrst}"
    echo
}

abort_on_error() {
    if [[ $? -ne 0 ]]; then
        echo_error "$1"
        exit 1
    fi
}

db_drop_db() {
  echo_message "DROPPING DB $1"

  dropdb \
      --host=${DATABASE_HOST} \
      --port=${DATABASE_PORT} \
      --username=${DATABASE_USER}  \
      --no-password \
      "$1"
}

db_create_db() {
  echo_message "Creating DB $1"

  createdb \
  --host=${DATABASE_HOST} \
  --port=${DATABASE_PORT} \
  --username=${DATABASE_USER}  \
  --no-password \
  --owner=${DATABASE_USER} \
  --encoding=UTF-8 \
  "$1"
}

reset() {
    echo_message 'Resetting database'

    db_drop_db "centrebull_dev"
    db_drop_db "centrebull_test"
    db_create_db "centrebull_dev"
    db_create_db "centrebull_test"

    abort_on_error "Reset failed, exiting."
}

clean() {
    reset
    echo_message "Running Migration"
    lein migratus migrate
}

export PGPASSWORD=${DATABASE_PASSWORD}

if [[ $# -eq 0 ]]; then
    usage
elif [ $1 = "clean" ]; then
    clean
elif [ $1 = "reset" ]; then
      reset
else
  usage
fi
