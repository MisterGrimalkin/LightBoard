#!/usr/bin/env bash
mvn clean package && sshpass -p raspberry scp -r target/ pi@192.168.0.74:lightboard && sshpass -p raspberry scp -r html/ pi@192.168.0.74:lightboard

