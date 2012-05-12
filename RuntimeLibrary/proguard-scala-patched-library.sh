#!/bin/bash

if [[ -d /System ]] ; then
	JAVA_CLASSES_JAR=/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar
else
	JAVA_CLASSES_JAR="<java.home>/lib/rt.jar"
fi

java -jar proguard.jar -libraryjars "$JAVA_CLASSES_JAR" -injar  scala-patched-library.jar -outjar scalight-library-proguarded.jar -printmapping scalight-library-proguarded.mapping @scalight-library.pro || exit 1

ls -l scalight-library*.jar
