import com.github.retronym.SbtOneJar
import com.typesafe.sbt.SbtStartScript

import sbt._
import Keys._

object StirlingBuild extends Build {
  import Dependencies._
  import Resolvers._

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization                              := "stirling",
    version                                   := "1.6.5-SNAPSHOT",
    scalaVersion                              := "2.10.0",
    javacOptions                             ++= Seq("-Xlint:unchecked"),
    crossScalaVersions                        := Seq("2.9.2", "2.10.0"),
    resolvers                                ++= Seq(laughingPanda, mpeltonen, valotrading),
    testListeners                            <<= (target).map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath))),
    parallelExecution in Test                 := false,
    exportJars                                := true,
    publishArtifact in (Compile, packageDoc)  := false,
    publishTo                                 := Some(Resolver.file("GitHub Pages", file("../maven-gh-pages/")))
  )

  lazy val stirling = Project(
    id        = "stirling",
    base      = file("."),
    settings  = commonSettings,
    aggregate = Seq(
      stirlingCore,
      stirlingFixConsole,
      stirlingFixPerftest
    )
  )

  lazy val stirlingCore = Project(
    id       = "stirling-core",
    base     = file("core"),
    settings = commonSettings ++ Seq(
      testOptions          += Tests.Argument(TestFrameworks.JUnit, "-v"),
      libraryDependencies ++= Seq(
        commonsLang,
        jdave,
        jdaveSbt,
        jline,
        jmock,
        jodaConvert,
        jodaTime,
        junitInterface,
        log4j,
        mockito,
        mongoJavaDriver,
        scalatest,
        silvertip
      )
    )
  )

  lazy val stirlingFixConsole = Project(
    id           = "stirling-fix-console",
    base         = file("fix-console"),
    dependencies = Seq(stirlingCore),
    settings     = commonSettings ++ SbtOneJar.oneJarSettings ++ Seq(
      artifactPath in SbtOneJar.oneJar  := new File("fix-console.jar"),
      libraryDependencies              ++= Seq(
        mockito,
        scalatest % "test"
      )
    )
  )

  lazy val stirlingFixPerftest = Project(
    id           = "stirling-fix-perftest",
    base         = file("fix-perftest"),
    dependencies = Seq(stirlingCore),
    settings     = commonSettings ++ SbtStartScript.startScriptForClassesSettings
  )

  object Resolvers {
    val laughingPanda = "Laughing Panda" at "http://maven.laughingpanda.org/maven2/"
    val mpeltonen     = "Mikko Peltonen" at "http://mpeltonen.github.com/maven/"
    val valotrading   = "Valo"           at "http://valotrading.github.com/maven/"
  }

  object Dependencies {
    val commonsLang     = "commons-lang"  %  "commons-lang"      % "2.4"
    val jdave           = "org.jdave"     %  "jdave-junit4"      % "1.1"    % "test"
    val jdaveSbt        = "jdave-sbt"     %% "jdave-sbt"         % "0.2"    % "test"
    val jline           = "jline"         %  "jline"             % "0.9.94"
    val jmock           = "org.jmock"     %  "jmock"             % "2.5.1"  % "test"
    val jodaConvert     = "org.joda"      %  "joda-convert"      % "1.2"
    val jodaTime        = "joda-time"     %  "joda-time"         % "2.1"
    val junitInterface  = "com.novocode"  %  "junit-interface"   % "0.10"   % "test"
    val log4j           = "log4j"         %  "log4j"             % "1.2.16"
    val mockito         = "org.mockito"   %  "mockito-core"      % "1.9.0"  % "test"
    val mongoJavaDriver = "org.mongodb"   %  "mongo-java-driver" % "2.7.2"
    val scalatest       = "org.scalatest" %% "scalatest"         % "1.9.1"
    val silvertip       = "silvertip"     %  "silvertip"         % "0.4.3"
  }
}
