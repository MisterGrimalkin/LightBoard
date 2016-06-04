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
if [ "$3" = "" ]
then
    echo "No Messages specified"
    exit
fi
if [ "$4" = "-replace" ]
then
    sh clearmessages.sh $1 $2
fi
curl -s -d "$3" http://$lightboard:8001/lightboard/scene/$1/group/$2/add
echo