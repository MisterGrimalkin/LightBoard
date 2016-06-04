#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
boardname=`curl -s http://$lightboard:8001/lightboard/system/name`
if [ $? -eq 0 ]
then
    echo "LightBoard already active"
    exit
fi
echo "Starting LightBoard @ $lightboard..."
sshpass -p raspberry ssh pi@$lightboard "cd /home/pi/lightboard; ./board.sh" &
echo