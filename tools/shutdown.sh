#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
echo "Shutdown LightBoard @ $lightboard..."
curl -s -d "" http://$lightboard:8001/lightboard/system/shutdown
if [ $? -eq 0 ]
then
    echo
    echo "OK"
else
    echo "Failed!"
fi