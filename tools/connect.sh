#!/usr/bin/env bash
if [ "$1" = "" ]
then
    if [ ! -f "lightboard.ip" ]
    then
        echo "Please specify an IP address"
        exit
    else
        lightboard=`cat lightboard.ip`
    fi
else
    lightboard=$1
fi
echo "LightBoard @ $lightboard..."
boardname=`curl -s http://${lightboard}:8001/lightboard/system/name`
if [ $? -eq 0 ]
then
    echo "$lightboard" > lightboard.ip
    echo "BOARD ACTIVE: $boardname"
else
    ping -c 1 ${lightboard} >/dev/null
    if [ $? -eq 0 ]
    then
        echo "$lightboard" > lightboard.ip
        echo "Board Offline"
    else
        echo "No host found"
    fi
fi