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
echo "Uploading File..."
sshpass -p raspberry scp "$2.xml" pi@$1:lightboard/scenes
sh select.sh $1 $2