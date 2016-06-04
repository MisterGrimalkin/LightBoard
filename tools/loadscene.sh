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
curl -s -d "" http://$lightboard:8001/lightboard/scene/$1/load
echo
