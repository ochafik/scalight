This is a trimmed-down version of the Scala library (see LICENSE for copyright information).

As a first step, it's probably better not to get rid of Product, Tuples & Functions.

THIS WILL BE REPLACED BY A LIBRARY AUTOMATICALLY TRIMMED-DOWN BY scalaxy-trimmer 

Source taken from Scala 2.10.0-SNAPSHOT roughly as of the 1st May 2012, with the following changes :
*   Commented all annotations (including @specialized) 
*   Renamed scala package to scalaLight (see TODO below)
*   Changed scala.Product.productIterator to return Array[Any]

TODO
====

Using a tool such as maven-shade-plugin, relocate / rename the following :
*   scalaLight package to scala
*   Product.productIterator2 to Product.productIterator
