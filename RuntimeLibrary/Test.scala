

object Test { 
  def main(args: Array[String]) = { 
    //println(Option(1).map(_ + 2))
    try {
      case class T(a: Int, b: Int)
      val t = T(1, 2)
      
      System.out.println(t)
      
      t match {
        case T(a, b) =>
          System.out.println("a = " + a + ", b = " + b)
      }
    } catch { case ex: Throwable =>
      ex.printStackTrace
    }
    //*/
  }
}
