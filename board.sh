#!/usr/bin/env bash
sudo nice -n -20 java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g -jar lightboard.jar $*
# -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5678