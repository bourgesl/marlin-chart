#!/bin/bash

source ./env.sh

export JAVA_HOME=$OPENJDK8

PATH=$JAVA_HOME/bin/:$PATH
export PATH

echo "Java version:"
java -version

