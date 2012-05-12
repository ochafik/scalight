import System._
import java.io._
import sys._
import process._
import java.util.zip._

import collection.JavaConversions._
import org.objectweb.asm._
import ASMUtils._
import IOUtils._

/**
 * MUST COMPILE SCALA WITH -no-specialization first !
 * http://asm.ow2.org/asm40/javadoc/user/
 */
object Trimmer extends App {
  val (jar, dest) = args match {
    case Array(j, d) => (j, d)
    case _ =>
      (
        //getProperty("user.home") +
        //"RuntimeLibrary/scala-patched-library.jar",
        "RuntimeLibrary/scalight-library-proguarded.jar",
        "RuntimeLibrary/scalight-library-proguarded-trimmed.jar"
      )
  }

  def analyzeJarDependencies(in: ZipInputStream): Map[String, Set[String]] = {
    val deps = new collection.mutable.HashMap[String, Set[String]]()
    var e: ZipEntry = null
  
    while ({ e = in.getNextEntry ; e != null }) {
      val f = e.getName
      if (f.endsWith(".class")) {
        val b = readBytes(in)
        deps += getReferencedClasses(b)
      }
    }
    deps.toMap
  }
  def analyzeJarDependencies(file: File): Map[String, Set[String]] = {
    val in = new ZipInputStream(new FileInputStream(file))
    val deps = analyzeJarDependencies(in)
    in.close
    deps
  }
  
  def rewriteJarWithoutAnnotations(in: ZipInputStream, out: ZipOutputStream, entries: Set[String]): Unit = {
    val classNamePat = "(.*?)\\.class".r
    var e: ZipEntry = null
  
    while ({ e = in.getNextEntry ; e != null }) {
      e.getName match {
        case f @ classNamePat(n) if entries.contains(n) =>
          out.putNextEntry(new ZipEntry(f))
          out.write(removeAnnotations(readBytes(in)))
          out.closeEntry
        case f =>
          //println("\tSkipped '" + f + "'") 
      }
    }
  }
  
  def rewriteJarWithoutAnnotations(source: File, dest: File, entries: Set[String]): Unit = {
    val in = new ZipInputStream(new FileInputStream(source))
    val out = new ZipOutputStream(new FileOutputStream(dest))
    
    rewriteJarWithoutAnnotations(in, out, entries)
    
    in.close
    out.close
  }
  
  object Roots {
    /*
    val rootClasses = Set[String](
      "scala/Option",
      "scala/Either",
      "scala/Left",
      "scala/Right",
      "scala/PartialFunction",
      "scala/runtime/VolatileObjectRef"
    )
    val rootPatterns = Set[String](
      "scala/Tuple\\d*",
      "scala/Function\\d*"
    )//.map(_.r)
    */
    val rootClasses = Set[String]()
    val rootPatterns = Set[String](
      "scala/[^/]+",
      "scala/runtime/.*"
    )
    def isRoot(n: String) =
      rootClasses.contains(n) ||
      rootPatterns.exists(n.matches(_))
  }
  
  def breakDepsAt =  "scala/(collection|util|sys|reflect|math|xml)/.*"
  
  def resolveDeps(n: String, out: java.util.Set[String]): Unit = {
    if (!n.matches(breakDepsAt)) {
      if (out.add(n)) {
        for (od <- deps.get(n); d <- od) {
          resolveDeps(d, out)
        }
      }
    }
  }
  def resolveDeps(nn: Set[String]): Set[String] = {
    val out = new java.util.HashSet[String]()
    for (n <- nn)
      resolveDeps(n, out)
    out.toSet
  }
  
  def printDeps(deps: Map[String, Set[String]], outFile: File) = {
    val out = new PrintWriter(outFile)
    for ((n, dd) <- deps) {
      out.println("DEPENDENCIES FOR " + n + ": ")
      for (d <- dd)
        out.println("\t" + d)
    }
    out.close
    println("Wrote '" + outFile + "'")
  }
  
  
  println("Processing " + jar)
  val deps = analyzeJarDependencies(new File(jar))
  
  printDeps(deps, new File("Trimmer/deps.out"))
  
  val roots = deps.keys.filter(Roots.isRoot(_)).toSet
  val resolved: Set[String] = resolveDeps(roots)
  
  printDeps(deps.collect({ 
    case (n, dd) if resolved.contains(n) =>
      (n, dd.filter(resolved.contains(_)))
  }), new File("Trimmer/resolvedDeps.out"))
  
  val retained = resolved.filter(d => deps.keys.contains(d))
  
  //println(retained.toSeq.map("\t" + _).sorted.mkString("\n"))
  println("Got " + roots.size + " roots, resolved to " + retained.size + " dependencies")
  
  rewriteJarWithoutAnnotations(new File(jar), new File(dest), retained)
  
  Seq("unzip", "-l", dest) #| Seq("wc", "-l") !
  
  Seq("ls", "-l", jar, dest) !
  
  //println(roots.toSeq.map("\t" + _).sorted.mkString("\n"))
  //Seq("unzip", jar, "-d", tmp.toString) ! 
}
