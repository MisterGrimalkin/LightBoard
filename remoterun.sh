#!/usr/bin/env bash
ip=18
echo
echo "Shutting down LightBoard..."
echo
curl -d "" http://192.168.0.$ip:8001/lightboard/system/shutdown
echo
sh deploy.sh
echo
echo "Starting LightBoard..."
echo
sshpass -p raspberry ssh pi@192.168.0.$ip "cd /home/pi/lightboard; ./board.sh >>lightboard.log 2>>lightboard.log" &
echo "Done."
echo