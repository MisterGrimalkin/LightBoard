#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
curl -s -d "" http://${lightboard}:8001/lightboard/system/test/squares
echo
