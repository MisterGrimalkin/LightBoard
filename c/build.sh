#!/usr/bin/env bash
echo "Compiling C...."
gcc -Wall -shared -lwiringPi -o liblightboard.so -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux lightboard.c
echo