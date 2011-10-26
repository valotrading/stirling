name := "xtch"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

javacOptions += "-Xlint:unchecked"

resolvers += "laughingpanda" at "http://www.laughingpanda.org/maven2/"

libraryDependencies ++= Seq(
  "org.jdave" % "jdave-junit4" % "1.3" % "test",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test"
)

testFrameworks += new TestFramework("org.jdave.sbt.JDaveFramework")
