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
cd ..
echo "Compiling Java..."
mvn clean package
echo "Uploading Java..."
sshpass -p raspberry scp -r target/ pi@${lightboard}:lightboard
echo "Uploading HTML..."
sshpass -p raspberry scp -r html/ pi@${lightboard}:lightboard
echo "Uploading C..."
sshpass -p raspberry scp -r src/main/c/ pi@${lightboard}:lightboard
sshpass -p raspberry ssh pi@${lightboard} "cd /home/pi/lightboard/c; chmod +x build.sh; chmod +x test.sh; ./build.sh"
echo "Deployed to ${lightboard}"
cd tools
if [ ${wasup} ]
then
    sh startup.sh
fi
