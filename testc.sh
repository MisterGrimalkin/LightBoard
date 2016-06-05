#!/usr/bin/env bash
classpath="/home/pi/lightboard/lib/*:/home/pi/lightboard/target/classes"
sudo java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g \
    -cp "$classpath" net.amarantha.lightboard.board.CLightBoard $*