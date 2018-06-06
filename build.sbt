organization := "com.colisweb.sbt"

name := "sbt-datadog"

scalaVersion := "2.12.6"

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4" % "provided")

credentials += Credentials(Path.userHome / ".bintray" / ".credentials")
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
homepage := Some(url("https://github.com/colisweb/sbt-datadog"))
bintrayOrganization := Some("colisweb")
bintrayReleaseOnPublish := true
publishMavenStyle := true
pomExtra := (
  <scm>
    <connection>scm:git@github.com:colisweb/sbt-datadog.git</connection>
    <developerConnection>scm:git@github.com:colisweb/sbt-datadog.git</developerConnection>
    <url>https://github.com/colisweb/sbt-datadog</url>
  </scm>
    <developers>
      <developer>
        <id>guizmaii</id>
        <name>Jules Ivanic</name>
        <url>https://www.colisweb.com</url>
      </developer>
    </developers>
)
