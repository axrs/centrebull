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
    ${txtbld}migrate${txtrst}
        Runs database migration.${txtrst}
    ${txtbld}test${txtrst}
        runs all unit tests.${txtrst}
    ${txtbld}test-refresh${txtrst}
        runs all unit tests and starts a watch.${txtrst}
    ${txtbld}setup${txtrst}
        sets up the application database for running and debugging.${txtrst}
    ${txtbld}shutdown${txtrst}
        terminates the application; shutting down the database.${txtrst}
    ${txtbld}run${txtrst}
        starts the database and runs the application, serving files and folders.${txtrst}
    ${txtbld}figwheel${txtrst}
        runs figwheel.${txtrst}
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
  vagrant ssh -c "export PGPASSWORD=${DATABASE_PASSWORD}; dropdb --host=localhost --port=5432 --username=${DATABASE_USER} --no-password  $1"
}

db_create_db() {
  echo_message "Creating DB $1"
  vagrant ssh -c "export PGPASSWORD=${DATABASE_PASSWORD}; createdb --host=localhost --port=5432 --username=${DATABASE_USER} --no-password --owner=${DATABASE_USER} --encoding=UTF-8 $1"
}

reset() {
    echo_message 'Resetting database'

    db_drop_db "centrebull_dev"
    db_drop_db "centrebull_test"
    db_create_db "centrebull_dev"
    db_create_db "centrebull_test"

    abort_on_error "Reset failed, exiting."
}

migrate() {
    echo_message "Running Migration"
    lein migratus migrate
}

clean() {
    reset
    migrate
}

check_exec_exists() {
    echo_message "Looking for $1..."

    if ! type "$1" > /dev/null; then
        echo_error "Unable to find $1... Aborting"
        exit 1
    fi

    echo_message "$1 found..."
}

build_docker_database() {
    echo_message "Building Docker Database Image"
    docker build -t cb-postgres ./tools/Postgres/
}

wait_for_docker() {
	echo_message "Waiting for docker to start"
	D=$(docker ps 2>&1)

	if [[ "$D" == *"daemon"* ]]; then
		sleep 5
		wait_for_docker
	fi
}

run_docker_database() {
    check_exec_exists "docker"
    echo_message "Opening Docker"
	open -a Docker
	wait_for_docker
    echo_message "Starting..."

    docker_image=$(docker images -q cb-postgres)

    if [ "$docker_image" ]; then
        echo_message "Using existing container"
        docker start cb-postgres
    else
        echo_message "No container found... Building new container"
        build_docker_database
        docker run -d -p 5432:5432 --name cb-postgres cb-postgres
    fi
}

stop_docker_database() {
    echo_message "Shutting down Database Image"
    docker stop cb-postgres
}

docker_clean() {
    stop_docker_database
    docker rm cb-postgres
    run_docker_database
    migrate
}

run() {
    run_docker_database
    lein run
}

test_refresh() {
    echo_message "Running Test Refresh"
    lein test-refresh
}

test() {
    echo_message "Testing code base"
    lein test
}

figwheel() {
    lein figwheel
}

parse () {
    if [[ $# -eq 0 ]]; then
        usage
    elif [ $1 = "clean" ]; then
        clean
    elif [ $1 = "docker-clean" ]; then
        docker_clean
    elif [ $1 = "reset" ]; then
        reset
    elif [ $1 = "migrate" ]; then
        migrate
    elif [ $1 = "test" ]; then
        test
    elif [ $1 = "test-refresh" ]; then
        test_refresh
    elif [ $1 = "setup" ]; then
        run_docker_database
        migrate
        run
    elif [ $1 = "shutdown" ]; then
        stop_docker_database
    elif [ $1 = "run" ]; then
        run
    elif [ $1 = "figwheel" ]; then
        figwheel
    else
      usage
    fi
}

export PGPASSWORD=${DATABASE_PASSWORD}

parse $@
