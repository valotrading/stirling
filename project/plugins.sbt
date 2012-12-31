resolvers ++= Seq(
  "retronym-releases" at "http://retronym.github.com/repo/releases",
  "junit_xml_listener Repository" at "http://valotrading.github.com/junit_xml_listener/maven/"
)

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")

addSbtPlugin("eu.henkelmann" % "junit_xml_listener" % "0.3")
