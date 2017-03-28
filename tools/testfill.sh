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
    echo "No Colour specified - specify red/green/yellow"
    exit
fi
curl -s -d "" http://${lightboard}:8001/lightboard/system/test/fill/$1
echo
