#!/bin/bash

if [[ -d scala-scalight ]] ; then
	cd scala-scalight || exit 1
	
	echo "Resetting sources"
	git checkout src || exit 1
	
	echo "Deleting previous builds"
	pwd
	ant all.clean || exit 1
	
else
	echo "Checking out scala"
	git checkout https://github.com/scala/scala.git scala-scalight
	
	cd scala-scalight || exit 1
	
	dos2unix *.sh tools/*.sh || exit 1
	
	if [[ -d /System ]] ; then 
		echo "Fixing mac build"
		#local version=$(echo $sha1 | sed 's/ \?.*//')
		#local version=${sha1% ?$jar_name}
	  
		sed -i.bak -E 's/\$\{sha1% \?\$jar_name\}/\$\(echo \$sha1 \| sed '"'s\/ \\\?.*\/\/'\)/" tools/binary-repo-lib.sh
	fi
	
	./pull-binary-libs.sh || exit 1
fi

SCALA_SCALIGHT_HOME=`pwd`

echo "Build scala"
ant || exit 1

echo "Applying first patch"
git apply ../scalight-compiler.diff || exit 1

echo "Deleting the 'locker' compiler"
rm -fR build/locker/all.complete build/locker/compiler.complete build/locker/classes/compiler

echo "Recompiling the 'locker' compiler"
ant || exit 1
	
echo "Applying second patch"
git apply ../scalight-library.diff || exit 1

echo "Removing @specialized annotations"
find src/library -name '*.scala' -exec sed -i.bak -E 's/@specialized(\([^)]+\))?//g' '{}' ';'
	
echo "Recompiling 'quick'"	
ant quick.clean build || exit 1

echo "Converting line endings of scripts"	
for B in scala scalac scalap scaladoc ; do
	dos2unix build/pack/bin/$B || exit 1
done

echo "Copying scala-patched-library.jar"
cp -f build/pack/lib/scala-library.jar ../scala-patched-library.jar || exit 1

cd ..

if [[ -d /System ]] ; then
	JAVA_CLASSES_JAR=/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar
else
	JAVA_CLASSES_JAR="<java.home>/lib/rt.jar"
fi

java -jar proguard.jar -libraryjars "$JAVA_CLASSES_JAR" -injar  scala-patched-library.jar -outjar scalight-library.jar -printmapping scalight-library.proguard.mapping @scalight-library.pro || exit 1

ls -l scalight-library.jar
