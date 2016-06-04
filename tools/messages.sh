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
if [ "$2" = "" ]
then
    echo "No Message Group specified"
    exit
fi
echo "Messages:"
curl -s http://${lightboard}:8001/lightboard/scene/$1/group/$2/list
echo
