#!/bin/bash

source ./env.sh

export JAVA_HOME=$OPENJDK8

PATH=$JAVA_HOME/bin/:$PATH
export PATH

export BOOTCP="-Xbootclasspath/a:/home/bourgesl/libs/marlin/marlin-chart/lib/marlin-0.9.4.3-Unsafe.jar -Dsun.java2d.renderer=org.marlin.pisces.DMarlinRenderingEngine -Dsun.java2d.renderer.log=true"

echo "Java version:"
java -version

