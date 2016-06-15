#!/usr/bin/env bash
#classpath="/home/pi/lightboard/lib/*:/home/pi/lightboard/target/classes"
#if [ "$1" = "-nolog" ]
#then
#    cd /home/pi/lightboard/target/lightboard
    sudo nice -n -20 java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g -jar lightboard.jar $*
#    sudo java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g -cp "/home/pi/lightboard/lib/*:/home/pi/lightboard/target/classes" net.amarantha.lightboard.Main $*
#else
#    if [ -f lightboard.log ]
#    then
#        lastmod=`stat -c %y lightboard.log`
#        lastmoddate=`echo ${lastmod} | cut -d' ' -f 1`
#        lastmodtime=`echo ${lastmod} | cut -d' ' -f 2 | cut -d'.' -f 1`
#        mv lightboard.log lightboard.${lastmoddate}T${lastmodtime}.log
#        mv lightboard-server.log lightboard-server.${lastmoddate}T${lastmodtime}.log
#    fi
#    sudo java -Djava.library.path=/home/pi/lightboard/c -client -Xms1g -Xmx1g \
#    -cp "$classpath" net.amarantha.lightboard.Main $* >>lightboard.log 2>>lightboard-server.log
#fi
