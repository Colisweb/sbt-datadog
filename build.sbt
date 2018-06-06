organization := "com.colisweb.sbt"

name := "sbt-datadog"

scalaVersion := "2.12.6"

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4" % "provided")

publishTo := Some("Colisweb sbt-publigs Bintray" at "https://dl.bintray.com/colisweb/sbt-plugins")
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
homepage := Some(url("https://github.com/colisweb/sbt-datadog"))
bintrayOrganization := Some("colisweb")
bintrayReleaseOnPublish := false
publishMavenStyle := false

