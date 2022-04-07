organization  := "com.guizmaii"
name          := "sbt-datadog"
version       := sys.env.getOrElse("RELEASE_VERSION", "0.0.1-SNAPSHOT")
versionScheme := Some("semver-spec")
scalaVersion  := "2.12.15"
sbtPlugin     := true

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9" % "provided")

homepage := Some(url("https://github.com/guizmaii/sbt-datadog")),
licenses   := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
developers := List(
  Developer(
    "guizmaii",
    "Jules Ivanic",
    "jules.ivanic@gmail.com",
    url("https://blog.jules-ivanic.com/"),
  )
)
