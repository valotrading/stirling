organization := "xtch"

name := "xtch"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository (releases)" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "silvertip" % "silvertip" % "0.2.1"
)

publishTo := Some(Resolver.file("GitHub Pages", file("../xtch-gh-pages/maven/")))

publishArtifact in (Compile, packageDoc) := false
