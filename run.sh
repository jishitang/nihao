#!/bin/sh
if [ $# != 1 ];then
  echo "Please provide doTransaction or setExchangeRate"
  exit 1
fi
java -cp 'apps/*:conf/:lib/*' cn.webank.demo.Application $1
