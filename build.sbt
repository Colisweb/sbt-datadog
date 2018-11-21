organization := "com.colisweb.sbt"

name := "sbt-datadog"

scalaVersion := "2.12.7"

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.13" % "provided")

credentials += Credentials(Path.userHome / ".bintray" / ".credentials")
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
homepage := Some(url("https://github.com/colisweb/sbt-datadog"))
bintrayOrganization := Some("colisweb")
bintrayReleaseOnPublish := true
publishMavenStyle := true
pomExtra := (
  <url>https://github.com/colisweb/sbt-datadog</url>
  <scm>
    <url>git@github.com:colisweb/sbt-datadog.git</url>
    <connection>scm:git:git@github.com:colisweb/sbt-datadog.git</connection>
  </scm>
  <developers>
    <developer>
      <id>guizmaii</id>
      <name>Jules Ivanic</name>
      <url>http://www.colisweb.com</url>
    </developer>
  </developers>
)
