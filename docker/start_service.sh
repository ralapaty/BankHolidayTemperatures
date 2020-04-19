#!/usr/bin/env bash
set -e
# start spring-boot
mkdir -p /app/logs
java -jar /app/bankholidays.jar  > /app/logs/application.log 2>&1 &
PID=$!

# print application.log to stdout
MESSAGES_LOG=/app/logs/application.log
while [ ! -f "$MESSAGES_LOG" ]; do sleep 1; done
tail -F "$MESSAGES_LOG" &

# gracefully stop all processes in this shell's process group
trap "stop_service" SIGINT SIGTERM
stop_service() {
  # stop the server first, wait for graceful termination
  kill -TERM $PID
  wait $PID
  # stop the script and all remaining child processes
  kill -TERM -$$
}
# block until the server runs
wait $PID
