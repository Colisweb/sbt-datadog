organization := "com.colisweb.sbt"
name := "sbt-datadog"
scalaVersion := "2.12.10"
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
homepage := Some(url("https://github.com/colisweb/sbt-datadog"))
sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.6.1" % "provided")
bintrayOrganization := Some("colisweb")
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
