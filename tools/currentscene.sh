#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
echo "Current Scene:"
curl -s http://${lightboard}:8001/lightboard/scene/current
echo