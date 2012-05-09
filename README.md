Scalight = Scala language MINUS scala-library

Not much left, but still way above Java !

*This is just at the concept phase right now, there's not even a prototype to try out, but your feedback is welcome (see below)*

Rationale
=========

**If Java is JVM's assembler, Scala is it's C++, and Scalight aims at being its C language.**

[Scala](http://www.scala-lang.org) is a great language with countless advantages, and it is rather tightly coupled to its great [runtime library](file://localhost/Users/ochafik/bin/scala-2.10.0.latest-devel-docs/api/index.html).

However, that library has grown big, as in 6-8MB big, and it's unlikely to ever go back below 5 MB.

That's okay for fat desktop or server apps, but not so much for Android or other embedded / real-time situations (where any library that creates lots of objects can be a threat anyway).

The goal of Scalight is to provide a minimalistic subset of the Scala language with virtually no runtime dependency on Scala-the-library (let's say less than 100kB of runtime dependencies as a first goal).

Of course, this means getting a language that's close to Scala by the syntax but closer to raw Java by the power (still a few times more concise than Java, though).

Although my primary goal is to make Scala a better contender on the Android platform, not having to depend on scala-library anymore could unlock some deciders in Java software houses... What do **you** think ? 

What would still be possible in Scalight ?
==========================================

Plan is to provide / keep the following features :
*   Java collection wrappers that roughly mimic (mutable) Scala collections, yet with absolutely no runtime dependencies (hehe, we've got some magic in stock to achieve that, please read on)
*   Closures
*   For loops... 
*   Case classes, tuples & their pattern-matching

How would it work ?
===================

We'd provide a **static library** that covers :
*   Minimalistic DSL that wraps Java collections and makes them look and feel like Scala collections : you'll get basic map, filter, foreach, reduce & fold, reverse, takeWhile & dropWhile, groupBy operations on java.util.List and java.util.Map
*   Minimalistic DSL for Dynamic type (aliased to Object) 
*   Ultra-minimalistic and naive parallel list (syntactic sugar for List of Futures)

That static library would, by definition, not be needed at runtime : the magic in place here would be a compiler plugin that inlines the static library methods straight into the compiled code, which doesn't depend on anything but regular Java APIs in the end. And since the static library only relies on type aliases of Java types, there's really nothing to depend upon at runtime !

To be more precise, that compiler plugin would just be a derivative from [Scalaxy](http://github.com/ochafik/Scalaxy), with many [compilets](https://github.com/ochafik/Scalaxy/wiki/Scalaxy-Compilets) to make the code dependency-free and an extra compiler phase that throws errors at whatever unsupported Scala API call that remains in the user code after the compilets.

These compilets would have to cover all of the static library and most (if not all) of scala.runtime.RichX classes. 

In addition to that static library, a minimalistic core-Scala runtime library would still contain some basic classes, hard (although not impossible) to avoid :
*   Tuples & Product (maybe not needed, could encode them with Object arrays)
*   Function & PartialFunction (maybe not needed, could enforce the use of inlineable closures)

As a future extension, a more extreme **scalighter** mode might be added with flattening of tuples and inlining of functions 

That's it !

Contribute / React
==================

[Ping me on Twitter](http://twitter.com/ochafik) !