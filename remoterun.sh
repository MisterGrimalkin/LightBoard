#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo
    echo "Please specify target IP address"
    echo
    exit
fi
echo
echo "Shutting down LightBoard..."
echo
curl -d "" http://$1:8001/lightboard/system/shutdown
echo
sh deploy.sh $*
echo
echo "Starting LightBoard..."
echo
sshpass -p raspberry ssh pi@$1 "cd /home/pi/lightboard; ./board.sh >>lightboard.log 2>>lightboard.log" &
echo "Done."
echo