#!/usr/bin/env bash
sudo java \
-Djava.library.path=/home/pi/lightboard/c \
-client \
-Xms1g \
-Xmx1g \
-cp "/home/pi/lightboard/lib/*:/home/pi/lightboard/target/classes" \
net.amarantha.lightboard.Main $*
