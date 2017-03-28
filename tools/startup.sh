#!/usr/bin/env bash
if [ ! -f "password" ]
then
    echo "No password set"
    exit 1
else
    password=`cat password`
fi


if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
boardname=`curl -s http://${lightboard}:8001/lightboard/system/name`
if [ $? -eq 0 ]
then
    echo "LightBoard already active"
    exit
fi
echo "Starting LightBoard @ $lightboard..."
sshpass -p ${password} ssh pi@${lightboard} "cd /home/pi/lightboard; ./board.sh >/dev/null 2>/dev/null" &
echo