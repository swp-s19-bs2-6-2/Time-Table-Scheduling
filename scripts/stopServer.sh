#!/usr/bin/env bash

echo 'Stop running server first'
pid=$(lsof -i:5000 -t); if [ "$(expr length "$pid")" -ne "0" ]; then
kill -TERM $pid || kill -KILL $pid
fi
