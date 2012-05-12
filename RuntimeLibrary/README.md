Differences :

*   Product.productIterator is java.util.Iterator[Any] instead of scala.collection.Iterator[Any]
*   No @specialized annotations

To build the patched scala-library :

	./build-scala-patched-library.sh
	
Then in sbt, use proguard to trim scala-patched-library.jar :

	generate-runtime
	
	
