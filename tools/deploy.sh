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
if [ "$1" != "-skipjava" ]
then
    echo "Compiling Java..."
    mvn clean package
    echo "Uploading Java..."
    sshpass -p raspberry scp -r target/lightboard/ pi@${lightboard}:
else
    echo "Uploading Native Sources..."
    sshpass -p raspberry scp -r src/main/c/ pi@${lightboard}:lightboard
fi
echo "Uploading Shell Scripts..."
sshpass -p raspberry scp *.sh pi@${lightboard}:lightboard/
sshpass -p raspberry ssh pi@${lightboard} "cd /home/pi/lightboard; chmod +x *.sh"
echo "Compiling Native Code..."
#sshpass -p raspberry scp -r src/main/c/ pi@${lightboard}:lightboard
sshpass -p raspberry ssh pi@${lightboard} "cd /home/pi/lightboard/c; chmod +x build.sh; chmod +x test.sh; bash build.sh"
echo "Deployed to ${lightboard}"
echo
cd tools
if [ ${wasup} ]
then
    sh startup.sh
fi
