#!/usr/bin/env bash
java \
-Djava.library.path=native/libpi4j.so \
-client \
-Xms1g \
-Xmx1g \
-cp "~/code/lightboard/lib/*:~/code/lightboard/target/classes" \
net.amarantha.lightboard.Main $*
