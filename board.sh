#!/usr/bin/env bash
if [ "$1" = "-nolog" ]
then
    sudo nice -n -20 java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g -jar lightboard.jar $*
else
    if [ -f lightboard.log ]
    then
        lastmod=`stat -c %y lightboard.log`
        lastmoddate=`echo ${lastmod} | cut -d' ' -f 1`
        lastmodtime=`echo ${lastmod} | cut -d' ' -f 2 | cut -d'.' -f 1`
        mv lightboard.log lightboard.${lastmoddate}T${lastmodtime}.log
        mv lightboard-server.log lightboard-server.${lastmoddate}T${lastmodtime}.log
    fi
    sudo nice -n -20 java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g -jar lightboard.jar $* \
    >>lightboard.log 2>>lightboard-server.log
fi
