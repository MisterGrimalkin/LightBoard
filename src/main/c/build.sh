#!/usr/bin/env bash
files=( "lightboard_192x32_small_sign" "lightboard_192x32_big_sign" )
echo "Compiling C...."
for i in "${files[@]}"
do
	echo ${i}
done
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
    for i in "${files[@]}"
    do
        gcc -shared -lwiringPi -o lib${i}.so ${jniInclude} ${jniLinux} ${i}.c
        gcc -lwiringPi -o ${i}.so ${jniInclude} ${jniLinux} ${i}.c
    done
fi
echo