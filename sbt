#!/bin/bash

CURL="curl"

JAVA="java"
JAVA_OPTS="-Xms512M -Xmx2048M -Xss1M -XX:MaxPermSize=512M"

SBT="sbt-launch.jar"
SBT_URL="http://typesafe.artifactoryonline.com/typesafe/ivy-releases/org.scala-tools.sbt/sbt-launch/0.11.2/sbt-launch.jar"

if [ ! -e $SBT ]; then
    $CURL -O -s $SBT_URL
fi

$JAVA $JAVA_OPTS -jar $SBT "$@"
