rm -fR target
mkdir target
#2> /dev/null
scala-scalight/build/pack/bin/scalac -no-specialization -d target Test.scala ScalaRunTime.scala || exit 1

for L in \
	scala-patched-library.jar \
	scalight-library-proguarded.jar \
	scalight-library-proguarded-trimmed.jar \
; do
	echo "#"
	echo "# With $L :"
	echo "#"
	java -cp target:$L Test
done
