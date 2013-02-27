#!/bin/bash

function fail() {
	echo "#"	
	echo "# ERROR: $@"	
	echo "#"
	exit 1
}

if [[ -d scala-scalight ]] ; then
	cd scala-scalight || exit 1
	
	echo "Resetting sources"
	git reset --hard || fail "Failed to revert sources in `pwd`"
	
	echo "Deleting previous builds"
	pwd
	ant all.clean || fail "Failed to clean"
	
else
	echo "Cloning scala"
	git clone https://github.com/scala/scala.git scala-scalight
	
	cd scala-scalight || exit 1
	git checkout 2.10.x || fail "failed to checkout branch"
	
	dos2unix *.sh tools/*.sh || fail "Failed to fix script line endings"
	
	if [[ -d /System ]] ; then 
		echo "Fixing mac build"
		#local version=$(echo $sha1 | sed 's/ \?.*//')
		#local version=${sha1% ?$jar_name}
	  
		#sed -i.bak -E 's/\$\{sha1% \?\$jar_name\}/\$\(echo \$sha1 \| sed '"'s\/ \\\?.*\/\/'\)/" tools/binary-repo-lib.sh
	fi
	
	./pull-binary-libs.sh || fail "Failed to pull binary libs"
fi

SCALA_SCALIGHT_HOME=`pwd`

echo "Compiler 'locker' lib"
ant locker.lib || fail "Failed to build scala"

echo "Applying first patch"
git apply ../scalight-compiler.diff || fail "Failed to apply compiler patch"
git apply ../scalight-reflect.diff || fail "Failed to apply reflect patch"

echo "Deleting the 'locker' compiler, if it exists"
rm -fR build/locker/all.complete build/locker/compiler.complete build/locker/classes/compiler
rm -fR build/locker/all.complete build/locker/reflect.complete build/locker/classes/reflect

echo "Compiling the 'locker' compiler"
ant locker.comp || fail "Failed to recompile the 'locker' compiler"
	
echo "Applying second patch"
git apply ../scalight-library.diff || fail "Failed to apply the second patch"

echo "Removing @specialized annotations"
find src/library -name '*.scala' -exec sed -i.bak -E 's/@specialized(\([^)]+\))?//g' '{}' ';'
	
echo "Recompiling 'quick'"	
ant quick.clean build || fail "Failed to recompile 'quick'"

echo "Converting line endings of scripts"	
for B in scala scalac scalap scaladoc ; do
	dos2unix build/pack/bin/$B || fail "Failed to fix bin scripts line endings"
done

echo "Copying scala-patched-library.jar"
cp -f build/pack/lib/scala-library.jar ../../scala-patched-library.jar || fail "Failed to copy jar"

cd ..

./proguard-scala-patched-library.sh
