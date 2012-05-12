

object Test { 
  def main(args: Array[String]) = { 
    //println(Option(1).map(_ + 2))
    
    //*
    case class T(a: Int, b: Int)
    val t = T(1, 2)
    
    println(t)
    
    t match {
      case T(a, b) =>
        println("a = " + a + ", b = " + b)
    }
    //*/
  }
}
