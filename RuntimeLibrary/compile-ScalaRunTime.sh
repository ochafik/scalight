#!/bin/bash

SCALA_SRC=scala-scalight1
SCALA_HOME=$SCALA_SRC/build/pack/

PATH=$SCALA_HOME/bin:$PATH

#scalac -d target $SCALA_SRC/src/library/scala/runtime/ScalaRunTime.scala

scalac -d target ScalaRunTime.scala

