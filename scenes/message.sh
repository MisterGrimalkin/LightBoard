#!/usr/bin/env bash
if [ "$4" = "clear" ]
then
    curl -d "" http://192.168.0.18:8001/lightboard/scene/test/message/1/clear
    curl -d "" http://192.168.0.18:8001/lightboard/scene/test/message/2/clear
    curl -d "" http://192.168.0.18:8001/lightboard/scene/test/message/3/clear
fi
curl -d "$1" http://192.168.0.18:8001/lightboard/scene/test/message/1/add
curl -d "$2" http://192.168.0.18:8001/lightboard/scene/test/message/2/add
curl -d "$3" http://192.168.0.18:8001/lightboard/scene/test/message/3/add