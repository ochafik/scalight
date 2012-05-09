package scalight

import scala.collection.JavaConversions._
  
package object collection {
  type Map[K, V] = java.util.Map[K, V]
  
  def Map[K, V](pairs: (K, V)*): Map[K, V] = 
  {
    val m = new java.util.HashMap[K, V]
    for ((k, v) <- pairs)
      m.put(k, v)
    m
  }
  /*
  implicit def mapImplicits[K, V](m: Map[K, V]) = new {
    def map[R](f: (K, V) => R): Map[K, V] = {
      TODO CanBuildFrom -> List, Map  
    }
  }
  */
  
  type List[T] = java.util.List[T]
  
  def List[T](items: T*): List[T] = 
  {
    val l = new java.util.ArrayList[T]
    for (v <- items)
      l.add(v)
    l
  }
  
  implicit def listImplicits[T](l: List[T]) = new {
    def foreach[U](f: T => U): Unit =
      l.foreach(f)
    
    def map[U](f: T => U): List[U] =
      l.map(f)
    
    def filter(f: T => Boolean): List[T] = 
      l.filter(f)
    
    def takeWhile(f: T => Boolean): List[T] = 
      l.takeWhile(f)
    
    def dropWhile(f: T => Boolean): List[T] = 
      l.dropWhile(f)
    
    def head: T =
      l.head
      
    def tail: List[T] =
      l.tail
      
    def reduceLeft(f: (T, T) => T): T =
      l.reduceLeft(f)
      
    def reduceRight(f: (T, T) => T): T =
      l.reduceRight(f)
      
    def foldLeft[Z](seed: Z)(f: (Z, T) => Z): Z =
      l.foldLeft(seed)(f)
      
    def foldRight[Z](seed: Z)(f: (T, Z) => Z): Z =
      l.foldRight(seed)(f)
  }
}
