#!/bin/bash

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

SERVER_NAME=`sed '/^#/d;/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
if [ -z "$SERVER_NAME" ]; then
	SERVER_NAME=`hostname`
fi

PIDS=`ps -e -o 'pid=' -o 'command='|grep java|grep "$CONF_DIR"|awk '{print $1}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit 1
fi

echo "Stopping the $SERVER_NAME ..."
for PID in $PIDS ; do
    kill $PID > /dev/null 2>&1
    echo "  PID: $PID"
done

echo -e "  Waiting PIDS to quit ...\c"
COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in $PIDS ; do
	PID_EXIST=`ps -p $PID|tail -n +2|wc -l`
	if [ "$PID_EXIST" -gt 0 ]; then
		COUNT=0
		break
	fi
    done
done
echo "OK!"
