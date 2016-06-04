#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
echo "Available scenes:"
curl -s http://$lightboard:8001/lightboard/scene/list
echo
