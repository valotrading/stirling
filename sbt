#!/bin/bash

CURL="curl"

JAVA="java"
JAVA_OPTS="-Xms512M -Xmx2048M -Xss1M -XX:MaxPermSize=512M"

SBT="sbt-launch.jar"
SBT_URL="http://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.12.1/sbt-launch.jar"

if [ ! -e $SBT ]; then
    $CURL -O -s $SBT_URL
fi

$JAVA $JAVA_OPTS -jar $SBT "$@"
