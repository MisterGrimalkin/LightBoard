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
    echo "No Font Name specified"
    exit
fi
sshpass -p raspberry scp "../fonts/$1.fnt" pi@${lightboard}:lightboard/fonts
if [ "$2" = "-reload" ]
then
    sh reload.sh
fi