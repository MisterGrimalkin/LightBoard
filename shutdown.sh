#!/usr/bin/env bash
echo
if [ "$1" = "" ]
then
    echo "Please specify an IP address"
    echo
    exit
fi
echo "Shutting down LightBoard at $1"
echo
curl -d "" http://$1:8001/lightboard/system/shutdown
echo
echo