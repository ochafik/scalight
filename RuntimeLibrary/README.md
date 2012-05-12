Differences with scala-library.jar :

*   Product.productIterator is of type `java.util.Iterator[Any]` instead of `scala.collection.Iterator[Any]`
*   No @specialized annotations
*   Retaining only basic classes (and whatever they need to operate): Product, Tuple, Function, PartialFunction, Option, Either, Predef...

To build the patched scala-library :

	./build-scala-patched-library.sh
	
Then in sbt, use proguard to trim scala-patched-library.jar :

	generate-runtime
	
	
