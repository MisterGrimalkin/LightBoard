#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo "Specify a delay time in seconds"
    exit 1
fi

while [ true ]
do
    ./testfill.sh red
    sleep $1
    ./testfill.sh green
    sleep $1
    ./testfill.sh yellow
    sleep $1
    ./testscan.sh red 300
    sleep $1
    ./testscan.sh green 300
    sleep $1
    ./testscan.sh yellow 300
    sleep $1
done