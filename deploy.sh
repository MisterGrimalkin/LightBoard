#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo
    echo "Please specify target IP address"
    echo
    exit
fi
echo
echo "Deploying LightBoard application to $1"
if [ "$2" != "skipjava" ]
then
    echo
    echo "Compiling Java..."
    echo
    mvn clean package >/dev/null
    echo "Uploading Java..."
    sshpass -p raspberry scp -r target/ pi@$1:lightboard
fi
echo
echo "Uploading HTML..."
sshpass -p raspberry scp -r html/ pi@$1:lightboard
echo
echo "Uploading C..."
sshpass -p raspberry scp -r src/main/c/ pi@$1:lightboard
echo
sshpass -p raspberry ssh pi@$1 "cd /home/pi/lightboard/c; chmod +x build.sh; chmod +x test.sh; ./build.sh"
echo "Deployed to $1"
echo
