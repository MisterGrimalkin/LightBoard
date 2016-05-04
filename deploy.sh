#!/usr/bin/env bash
mvn clean package
sshpass -p raspberry scp -r target/ pi@192.168.0.22:lightboard
sshpass -p raspberry scp -r html/ pi@192.168.0.22:lightboard

