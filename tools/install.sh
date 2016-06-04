#!/usr/bin/env bash
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
    wasup=true
    sh shutdown.sh
fi
echo "Clearing existing installation..."
sshpass -p raspberry ssh pi@${lightboard} "cd /home/pi; rm -r lightboard; mkdir lightboard;"
echo "Uploading Scenes..."
sshpass -p raspberry scp -r ../images pi@${lightboard}:lightboard
sshpass -p raspberry scp -r ../scenes pi@${lightboard}:lightboard
echo "Uploading Properties..."
sshpass -p raspberry scp ../default.properties pi@${lightboard}:lightboard/application.properties
echo "Uploading Libraries..."
sshpass -p raspberry scp -r ../lib pi@${lightboard}:lightboard
echo "Deploying Application..."
sh deploy.sh
if [ ${wasup} ]
then
    sh startup.sh
fi
