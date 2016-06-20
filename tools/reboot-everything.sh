#!/usr/bin/env bash
echo "Shutting Down Lightboards..."
sh connect.sh 192.168.1.71
sh shutdown.sh
echo
sh connect.sh 192.168.1.73
sh shutdown.sh
echo
sh connect.sh 192.168.1.74
sh shutdown.sh
echo
sh connect.sh 192.168.1.75
sh shutdown.sh
echo
echo "Shutting Down Server..."
sh server/shutdown.sh
echo
echo
echo "Rebooting Hosts..."
sshpass -p raspberry ssh pi@192.168.1.71 "sudo reboot"
sshpass -p raspberry ssh pi@192.168.1.72 "sudo reboot"
sshpass -p raspberry ssh pi@192.168.1.73 "sudo reboot"
sshpass -p raspberry ssh pi@192.168.1.74 "sudo reboot"
sshpass -p raspberry ssh pi@192.168.1.75 "sudo reboot"
sshpass -p raspberry ssh pi@192.168.1.76 "sudo reboot"
echo
echo "Everything will probably be OK again fairly soon."
echo





