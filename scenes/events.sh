#!/usr/bin/env bash
curl -d "" http://192.168.0.12:8001/lightboard/scene/test/group/events/clear
curl -d "{red}Monday,{green}1:00pm,Monday's Blue" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Tuesday,{green}2:00pm,Tuesday's Grey" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Wednesday,{green}3:00pm,Wednesday Too" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Thursday,{green}4:00pm,Couldn't Give a Fuck" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Friday,{green}5:00pm,I'm In {red}Love" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Saturday,{green}6:00pm,W a i t !" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
curl -d "{red}Sunday,{green}7:00pm,Always Comes Too Late" http://192.168.0.12:8001/lightboard/scene/test/group/events/add
