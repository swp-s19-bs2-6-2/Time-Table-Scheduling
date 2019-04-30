#!/usr/bin/env bash

#echo 'Stop running server first';
#pid=$(lsof -i:8081 -t);
#echo "PID is ";
#echo $pid;
#echo "PID length is";
#echo expr length "$pid";
#echo "Attempting to kill";
#if [[ "$(expr length "$pid")" -ne "0" ]]; then
#kill -KILL $pid; else
#echo 'No running server found'
#fi

if [ -f ~/server_pid.txt ]; then
echo "$(< ~/server_pid.txt)"
fi

#                         sh 'chmod +x ./scripts/stopServer.sh'
#                         sh './scripts/stopServer.sh'