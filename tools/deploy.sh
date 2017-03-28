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

if [ "$1" = "-clean" ]
then
    read -p "Really remove existing installation? " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]
    then
        exit 0
    fi
fi

boardname=`curl -s http://${lightboard}:8001/lightboard/system/name`
if [ $? -eq 0 ]
then
    wasup=true
    sh shutdown.sh
fi

if [ "$1" != "-skipjava" ]
then
    echo "Compiling Java..."
    cd ..
    mvn clean package
    cd tools
    if [ ! -f "../target/lightboard/lightboard.jar" ]
    then
        echo "Compilation Failed!"
        exit 1
    fi
fi

if [ "$1" = "-clean" ]
then
    echo "Removing existing installation..."
    sshpass -p ${password} ssh pi@${lightboard} "cd /home/pi; sudo cp lightboard/application.properties .; sudo rm -r lightboard; mkdir lightboard; sudo chmod a+xw lightboard; mv application.properties lightboard"
    echo "Uploading Application and Libraries..."
    sshpass -p ${password} scp -r ../target/lightboard pi@${lightboard}:
    echo "Uploading Configuration..."
    sshpass -p ${password} scp ../*config.json pi@${lightboard}:lightboard
else
    echo "Uploading Application JAR..."
    sshpass -p ${password} scp ../target/lightboard/*.jar pi@${lightboard}:lightboard
    echo "Uploading Native Sources..."
    sshpass -p ${password} scp -r ../target/lightboard/c pi@${lightboard}:lightboard
    echo "Uploading Fonts..."
    sshpass -p ${password} scp -r ../target/lightboard/fonts pi@${lightboard}:lightboard
    echo "Uploading Images..."
    sshpass -p ${password} scp -r ../target/lightboard/images pi@${lightboard}:lightboard
    echo "Uploading Scenes..."
    sshpass -p ${password} scp -r ../target/lightboard/scenes pi@${lightboard}:lightboard
    echo "Uploading Support Scripts..."
    sshpass -p ${password} scp -r ../target/lightboard/scripts pi@${lightboard}:lightboard
    sshpass -p ${password} ssh pi@${lightboard} "cd /home/pi/lightboard/scripts; sudo chmod +x *.sh"
fi

echo "Uploading Main Scripts and Default Properties..."
sshpass -p ${password} scp ../*.sh pi@${lightboard}:lightboard
sshpass -p ${password} ssh pi@${lightboard} "cd /home/pi/lightboard; sudo chmod +x *.sh; sudo chmod -x *.jar"
sshpass -p ${password} scp ../default.properties pi@${lightboard}:lightboard

echo "Compiling Native Libraries..."
sshpass -p ${password} ssh pi@${lightboard} "cd /home/pi/lightboard/c; chmod +x build.sh; ./build.sh; rm *.c; rm build.sh"

echo
echo "Deployed to ${lightboard}"
echo
if [ ${wasup} ]
then
    sh startup.sh
fi
