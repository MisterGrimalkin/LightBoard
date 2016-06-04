#!/usr/bin/env bash
if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi
sshpass -p raspberry scp pi@${lightboard}:lightboard/lightboard-server.log ./${lightboard}-server.log
less ${lightboard}-server.log
