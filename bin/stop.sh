#!/bin/bash

SERVICE_APP=sumscope.cdh.realtime.gateway.quickfix.Application
SERVICE_NAME=quickfixApplication

PROCESSNUM=`ps -ef | grep ${SERVICE_APP} | grep -v grep | wc -l`

if [ $PROCESSNUM -gt 0 ]
    then
        kill -9 `ps -ef | grep ${SERVICE_APP} | grep -v grep | awk '{print $2}'`
        echo "${SERVICE_NAME}...stop success"
    else
    echo "${SERVICE_NAME} is not running...stop failed"
fi
