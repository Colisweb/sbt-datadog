organization := "com.colisweb.sbt"

name := "sbt-datadog"

scalaVersion := "2.12.6"

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4" % "provided")

publishTo := Some("Colisweb public bintray" at "https://dl.bintray.com/colisweb/public")

credentials += Credentials(Path.userHome / ".bintray" / ".credentials")

lazy val bintraySettings = Seq(
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/colisweb/sbt-datadog")),
  bintrayOrganization := Some("colisweb"),
  bintrayReleaseOnPublish := false,
  publishMavenStyle := true,

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
        </developer>
      </developers>
    )
)