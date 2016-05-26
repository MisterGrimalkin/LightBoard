#!/usr/bin/env bash
ip=192.168.0.18
if [ "$4" = "clear" ]
then
    curl -d "$1" http://${ip}:8001/lightboard/scene/test/zone/1/replace
    curl -d "$2" http://${ip}:8001/lightboard/scene/test/zone/2/replace
    curl -d "$3" http://${ip}:8001/lightboard/scene/test/zone/3/replace
else
    curl -d "$1" http://${ip}:8001/lightboard/scene/test/zone/1/add
    curl -d "$2" http://${ip}:8001/lightboard/scene/test/zone/2/add
    curl -d "$3" http://${ip}:8001/lightboard/scene/test/zone/3/add
fi
echo
