Differences with scala-library.jar :

*   Product.productIterator is of type `java.util.Iterator[Any]` instead of `scala.collection.Iterator[Any]`
*   No @specialized annotations
*   Retaining only basic classes (and whatever they need to operate): Product, Tuple, Function, PartialFunction, Option, Either, Predef...

To build the patched scala-library :

	cd RuntimeLibrary
	./build-scala-patched-library.sh
	
That script will checkout scala from master, will apply minor patches (about productIterator & -no-specialize) in 2 steps (one to build the locker compiler, a second time to build the quick library with the freshly built compiler).

Then, the resulting scala-library.jar is copied as scala-patched-library.jar, and processed by proguard with an extensive list of things we want to keep :
*   Function, Products and Tuples of any arity
*   PartialFunction
*   Option
*   Either, Left, Right

The resulting scalight-library-proguarded.jar is then trimmed by the Trimmer sub-project and scalight-library-proguarded-trimmed.jar is created.
To launch the trimmer continuously, open a separate terminal and type :

	sbt -sbt-snapshot "project scalight-trimmer" ~run
	
One can test stuff with :

	cd RuntimeLibrary
	./test.sh
