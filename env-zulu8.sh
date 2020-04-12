#!/bin/bash

source ./env.sh

export JAVA_HOME=$ZULU8

PATH=$JAVA_HOME/bin/:$PATH
export PATH

export BOOTCP="-Dsun.java2d.renderer.log=true"

echo "Java version:"
java -version

