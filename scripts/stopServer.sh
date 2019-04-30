#!/usr/bin/env bash

echo 'Stop running server first'
pid=$(lsof -i:8081 -t); if [[ "$(expr length "$pid")" -ne "0" ]]; then
kill -KILL $pid; else
echo 'No running server found'
fi

#stage('Stop server') {
#             steps {
#                 sh 'chmod +x ./scripts/stopServer.sh'
#                 sh './scripts/stopServer.sh'
#             }
#         }