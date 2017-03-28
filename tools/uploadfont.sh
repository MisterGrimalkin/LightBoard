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
if [ "$1" = "" ]
then
    echo "No Font Name specified"
    exit
fi
sshpass -p ${password} scp "../fonts/$1.fnt" pi@${lightboard}:lightboard/fonts
if [ "$2" = "-reload" ]
then
    sh reload.sh
fi