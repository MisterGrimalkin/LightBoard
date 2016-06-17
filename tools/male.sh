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
    echo "No Ticket Number specified"
    exit
fi
sh clearmessages.sh showers male
sh addmessages.sh showers male "$1;;$1"
echo
