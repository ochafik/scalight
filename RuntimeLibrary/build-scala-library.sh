#!/bin/bash

if [[ -d scala-scalight ]] ; then
	cd scala-scalight
	
	echo "Resetting sources"
	git checkout src
	
	echo "Deleting previous builds"
	ant all.clean
	
	cd ..
else
	echo "Checking out scala"
	git checkout https://github.com/scala/scala.git scala-scalight
	
	cd scala-scalight
	
	dos2unix *.sh tools/*.sh
	
	if [[ -d /System ]] then 
		echo "Fixing mac build"
		#local version=$(echo $sha1 | sed 's/ \?.*//')
		#local version=${sha1% ?$jar_name}
	  
		sed -i.bak -E 's/\$\{sha1% \?\$jar_name\}/\$\(echo \$sha1 \| sed '"'s\/ \\\?.*\/\/'\)/" tools/binary-repo-lib.sh
	fi
	
	./pull-binary-libs.sh
fi

SCALA_SCALIGHT_HOME=`pwd`

echo "Build scala"
ant	

echo "Applying first patch"
git apply ../scalight-compiler.diff

echo "Deleting the 'locker' compiler"
rm -fR build/locker/all.complete build/locker/compiler.complete build/locker/classes/compiler

echo "Recompiling the 'locker' compiler"
ant
	
echo "Applying second patch"
git apply ../scalight-library.diff

echo "Removing @specialized annotations"
find src/library -name '*.scala' -exec sed -i.bak -E 's/@specialized\(.*\)//' '{}' ';'
	
echo "Recompiling 'quick'"	
ant quick.clean build
