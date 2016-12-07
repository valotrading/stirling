resolvers ++= Seq(
  "retronym"           at "http://retronym.github.com/repo/releases/",
  "junit_xml_listener" at "http://valotrading.github.com/junit_xml_listener/maven/"
)

addSbtPlugin("com.github.retronym" % "sbt-onejar"         % "0.8")

addSbtPlugin("eu.henkelmann"       % "junit_xml_listener" % "0.3")

addSbtPlugin("com.typesafe.sbt"    % "sbt-start-script"   % "0.6.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")
