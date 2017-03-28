#!/usr/bin/env bash
if [ ! -f "password" ]
then
    echo "No password set"
    exit 1
else
    password=`cat password`
fi

if [ ! -f "lightboard.ip" ]
then
    echo "No LightBoard specified"
    exit
else
    lightboard=`cat lightboard.ip`
fi

sshpass -p ${password} scp pi@${lightboard}:lightboard/lightboard.log ./${lightboard}.log
less ${lightboard}.log
