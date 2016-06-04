#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
if [ "$1" = "" ]
then
    echo "No Scene Name specified"
    exit
fi
sshpass -p raspberry scp "../scenes/$1.xml" pi@${lightboard}:lightboard/scenes
if [ "$2" = "-start" ]
then
    sh loadscene.sh $1
fi