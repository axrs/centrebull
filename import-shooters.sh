#!/usr/bin/env bash


cat ./resources/data/shooters.csv | while read line
do
        sid=$(echo $line | cut -d',' -f2)
        first=$(echo $line | cut -d',' -f3)
        last=$(echo $line | cut -d',' -f4)
        pref=$(echo $line | cut -d',' -f5)
        club=$(echo $line | cut -d',' -f6)

        echo "$sid, $first, $last, $pref, $club"
        curl -H "Content-Type: application/json" -X POST -d "{\"shooter/sid\":\"$sid\",\"shooter/last-name\":\"$last\",\"shooter/first-name\":\"$first\",\"shooter/preferred-name\":\"$pref\",\"shooter/club\":\"$club\"}" http://localhost:3000/shooters
done
