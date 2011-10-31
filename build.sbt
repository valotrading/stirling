name := "xtch"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

javacOptions += "-Xlint:unchecked"

resolvers += "Typesafe Repository (snapshots)" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "silvertip" % "silvertip" % "0.2.1"
)
