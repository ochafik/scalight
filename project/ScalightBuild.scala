import sbt._
import Keys._

object ScalightBuild extends Build 
{
  // TODO maven-shade-plugin to relocate RuntimeLibrary's scalaLight package to scala 
  
  override lazy val settings = super.settings ++ Seq(
    shellPrompt := { s => Project.extract(s).currentProject.id + "> " }
  ) ++ scalaSettings

  lazy val standardSettings =
    Defaults.defaultSettings ++ 
    infoSettings ++
    compilationSettings ++ 
    mavenSettings ++
    scalaSettings ++
    commonDepsSettings
    
  lazy val infoSettings = Seq(
    organization := "com.nativelibs4java",
    version := "0.3-SNAPSHOT",
    licenses := Seq("BSD-3-Clause" -> url("http://www.opensource.org/licenses/BSD-3-Clause")),
    homepage := Some(url("http://ochafik.com/blog/"))
  )
  lazy val compilationSettings = Seq(
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    javacOptions ++= Seq("-Xlint:unchecked") 
  )
  lazy val mavenSettings = Seq(
    publishMavenStyle := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("-SNAPSHOT")) 
        Some("snapshots" at nexus + "content/repositories/snapshots") 
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    pomIncludeRepository := { _ => false }
  )
  
  lazy val scalaSettings = Seq(
    //scalaVersion := "2.10.0-SNAPSHOT",
    scalaVersion := "2.10.0-M3",
    //scalaHome := Some(file("/Users/ochafik/bin/scala-2.10.0.latest"))
    //crossScalaVersions := Seq("2.10.0-M2"),
    
    resolvers += Resolver.sonatypeRepo("snapshots")
    //exportJars := true, // use jars in classpath
  )
  lazy val commonDepsSettings = Seq(
    libraryDependencies += "junit" % "junit" % "4.10" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"
    //libraryDependencies <+= scalaVersion(v => "org.scalatest" % ("scalatest_2.9.1"/* + v*/) % "1.7.1" % "test")
    //libraryDependencies += "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test"
  )
  
  
  val generateRuntime = TaskKey[Unit]("generate-runtime", "Trims down scala-library.jar into scalight-library.jar")

  val generateRuntimeTask = generateRuntime := {
    import sys.process._
    
    Seq(
      "java", "-jar", "RuntimeLibrary/proguard.jar",
      "-libraryjars",
      if (System.getProperty("os.name").contains("Mac OS"))
         "/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar"
      else
        "<java.home>/lib/rt.jar",
      "-injar",  "RuntimeLibrary/scala-library-no-specialization.jar",
      "-outjar", "RuntimeLibrary/scalight-library.jar",
      "-printmapping", "RuntimeLibrary/scalight-library.proguard.mapping", 
      
      "@RuntimeLibrary/scalight-library.pro"
    ) !
  }
  
  lazy val scalight = 
    Project(id = "scalight", base = file("."), settings = standardSettings ++ Seq(
      generateRuntimeTask,
      scalacOptions in console in Compile <+= (packageBin in compilerPlugin in Compile) map("-Xplugin:" + _)
    )).
    dependsOn(staticLibrary, compilets, compilerPlugin).
    aggregate(staticLibrary, compilets, compilerPlugin)
  
  lazy val compilerPlugin = 
    Project(id = "scalight-compiler-plugin", base = file("CompilerPlugin"), settings = standardSettings ++ Seq(
      libraryDependencies += "com.nativelibs4java" %% "scalaxy-compiler-plugin" % "0.3-SNAPSHOT"
    )).
    dependsOn(staticLibrary, compilets)

  /*
  lazy val runtimeLibrary = 
    Project(id = "scalight-runtime-library", base = file("RuntimeLibrary"), settings = standardSettings ++ Seq(
      scalacOptions ++= Seq("-bootclasspath", "RuntimeLibrary")
    ))// ++ proguard)
  */        
  lazy val staticLibrary = 
    Project(id = "scalight-static-library", base = file("StaticLibrary"), settings = standardSettings)
                 
  lazy val compilets = 
    Project(id = "scalight-compilets", base = file("Compilets"), settings = standardSettings ++ Seq(
      libraryDependencies += "com.nativelibs4java" %% "scalaxy-macros" % "0.3-SNAPSHOT"
    )).
    dependsOn(staticLibrary)
  
  /*
  lazy val trimmer =
    Project(id = "scalight-trimmer", base = file("Trimmer"), settings = standardSettings ++ Seq(
      libraryDependencies += "org.ow2.asm" % "asm" % "4.0"
    ))
  */  
  
  /*
  import ProguardPlugin._
  lazy val proguard = proguardSettings ++ Seq(
    proguardOptions := Seq("-dontoptimize -dontobfuscate")
  )
  */
}
