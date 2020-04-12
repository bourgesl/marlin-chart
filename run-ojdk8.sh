#!/bin/bash

export BOOTCP=""

source ./env-ojdk8.sh

JAVA_OPTS="-verbose:gc -Xms1g -Xmx1g"

java $BOOTCP $JAVA_OPTS -jar target/marlin-chart-0.0.1-jar-with-dependencies.jar

echo "done"

