#!/usr/bin/env bash
echo "Compiling C...."
# Pi2
jniInclude="NOT FOUND"
if [ -f /usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/jni.h ]
then
    jniInclude="-I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include"
    jniLinux="-I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux"
elif [ -f /usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include/jni.h ]
then
    jniInclude="-I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include"
    jniLinux="-I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include/linux"
fi
if [ "$jniInclude" == "NOT FOUND" ]
then
    echo "No Java Native Interface found"
else
    gcc -shared -lwiringPi -o liblightboard.so ${jniInclude} ${jniLinux} lightboard.c
    gcc -lwiringPi -o lightboard.so ${jniInclude} ${jniLinux} lightboard.c
fi
echo