#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
sshpass -p raspberry ssh pi@${lightboard} "cd /home/pi/lightboard; sudo chmod a+w application.properties"
sshpass -p raspberry scp pi@${lightboard}:lightboard/application.properties ./${lightboard}.properties
gedit ${lightboard}.properties
sshpass -p raspberry scp ./${lightboard}.properties pi@${lightboard}:lightboard/application.properties
rm ${lightboard}.properties