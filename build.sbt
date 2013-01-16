organization := "stirling"

name := "stirling"

version := "1.3.3-SNAPSHOT"

scalaVersion := "2.10.0"

javacOptions ++= Seq("-Xlint:unchecked")

crossScalaVersions := Seq("2.9.2", "2.10.0")

resolvers ++= Seq(
  "Laughing Panda Repository" at "http://maven.laughingpanda.org/maven2/",
  "Silvertip Repository" at "http://valotrading.github.com/silvertip/maven/",
  "Twitter Repository" at "http://maven.twttr.com/",
  "mpeltonen.github.com" at "http://mpeltonen.github.com/maven"
)

libraryDependencies ++= Seq(
  "com.twitter" % "util-logging" % "3.0.0",
  "commons-lang" % "commons-lang" % "2.4",
  "jline" % "jline" % "0.9.94",
  "jdave-sbt" %% "jdave-sbt" % "0.2" % "test",
  "joda-time" % "joda-time" % "2.1",
  "log4j" % "log4j" % "1.2.16",
  "org.jdave" % "jdave-junit4" % "1.1" % "test",
  "org.jmock" % "jmock" % "2.5.1" % "test",
  "org.joda" % "joda-convert" % "1.2",
  "org.mockito" % "mockito-core" % "1.9.0" % "test",
  "org.mongodb" % "mongo-java-driver" % "2.7.2",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "silvertip" % "silvertip" % "0.3.0"
)

testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath)))

testFrameworks += new TestFramework("org.jdave.sbt.JDaveFramework")

parallelExecution in Test := false

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

artifactPath in com.github.retronym.SbtOneJar.oneJar := new File("stirling.jar")

mainClass in com.github.retronym.SbtOneJar.oneJar := Some("stirling.console.ConsoleClient")

TaskKey[File]("make-perftest") <<= (baseDirectory, fullClasspath in Runtime) map { (base, classpath) =>
  val template = """|#!/bin/sh
    |JARS="%s"
    |OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"
    |java $OPTS -classpath "$JARS" %s $@""".stripMargin
  val mainClassName = "stirling.fix.performance.PerformanceTest"
  val classpathList = classpath.files.absString
  val outputFile = base / "scripts/perftest"
  IO.write(outputFile, template.format(classpathList, mainClassName))
  outputFile.setExecutable(true)
  outputFile
}

publishTo := Some(Resolver.file("GitHub Pages", file("../maven-gh-pages/")))

publishArtifact in (Compile, packageDoc) := false
