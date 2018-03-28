

name := "scalaReadingFromxlsheet"

version := "0.1"

//scalaVersion := "2.10.7"
scalaVersion := "2.11.2"

//assemblySettings

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)


libraryDependencies ++= Seq(
  "org.apache.poi" % "poi-ooxml" % "3.11",
  "org.apache.poi" % "poi" % "3.11",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test ,
"com.eed3si9n" % "sbt-assembly" % "0.14.3"
)
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")



initialCommands in console += " import org.apache.poi._"

exportJars := true

mainClass in Compile := Some("brd.readxls")
mainClass in(Compile, run) := Some("brd.readxls")
mainClass in(Compile, packageBin) := Some("brd.readxls")
mainClass in assembly := Some("brd.readxls")