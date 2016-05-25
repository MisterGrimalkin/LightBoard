#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo
    echo "Please specify target IP address"
    echo
    exit
fi
if [ "$2" = "" ]
then
    echo
    echo "Please specify scene name"
    echo
    exit
fi
echo "Activating Scene..."
echo
curl -d "" http://$1:8001/lightboard/scene/$2/load
echo
echo
