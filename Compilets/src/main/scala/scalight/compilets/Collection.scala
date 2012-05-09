package scalight; package compilets

import scalaxy.macros._
//import scalaxy.matchers._

object Collection extends scalaxy.Compilet 
{
  import scalight.collection._
  import scalight.collection.{ List => SList }
  
  
  def listForeach[T : TypeTag, U : TypeTag](l: SList[T], body: U) = replace(
    l.foreach(v => body),
    {
      val it = l.iterator()
      while (it.hasNext()) {
        val v = it.next
        body
      }
    }
  )
  // TODO...
}
