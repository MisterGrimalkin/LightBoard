#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
curl -s -d "" http://${lightboard}:8001/lightboard/scene/resume
echo
if [ $? -ne 0 ]
then
    echo "Failed!"
fi