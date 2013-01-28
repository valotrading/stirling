#!/bin/bash

CURL="curl"

JAVA="java"
JAVA_OPTS="-Xms512M -Xmx2048M -Xss1M -XX:MaxPermSize=512M"

SBT_VERSION=$(grep sbt.version project/build.properties | sed -e "s/sbt.version=//g")
SBT="sbt-launch-$SBT_VERSION.jar"
SBT_URL="http://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/$SBT_VERSION/sbt-launch.jar"

if [ ! -e $SBT ]; then
    $CURL -o $SBT -s $SBT_URL
fi

$JAVA $JAVA_OPTS -jar $SBT "$@"
