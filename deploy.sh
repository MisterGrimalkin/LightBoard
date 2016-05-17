#!/usr/bin/env bash
ip=10
echo
echo "Deploying LightBoard application to 192.168.0.$ip"
if [ "$1" != "skipjava" ]
then
    echo
    echo "Compiling Java..."
    echo
    mvn clean package >/dev/null
    echo "Uploading Java..."
    sshpass -p raspberry scp -r target/ pi@192.168.0.$ip:lightboard
fi
echo
echo "Uploading HTML..."
sshpass -p raspberry scp -r html/ pi@192.168.0.$ip:lightboard
echo
echo "Uploading C..."
sshpass -p raspberry scp -r c/ pi@192.168.0.$ip:lightboard
echo
sshpass -p raspberry ssh pi@192.168.0.$ip "cd /home/pi/lightboard/c; chmod +x build.sh; chmod +x test.sh; ./build.sh"
echo "Deployed to 192.168.0.$ip"
echo
